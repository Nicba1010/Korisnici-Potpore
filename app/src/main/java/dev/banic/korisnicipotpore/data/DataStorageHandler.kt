package dev.banic.korisnicipotpore.data

import android.content.Context
import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.data.Api.CompanyPaymentDataRequest
import dev.banic.korisnicipotpore.util.NetworkUtil
import dev.banic.korisnicipotpore.util.enqueue
import kotlin.properties.Delegates

object DataStorageHandler {
    fun getListCount(
        context: Context,
        callback: (Int) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        val allData: MutableAllCompanyData = FileStorage.readAllData(context)

        callback(allData.size)

        if (NetworkUtil.isNetworkConnected(context)) {
            var currentIndex = allData.size - 1

            // Hack to be able to reference the variable inside of itself
            var availabilityCallback: (Boolean) -> Unit by Delegates.notNull()
            availabilityCallback = { available ->
                if (available) {
                    currentIndex++
                    isDataForDateAvailable(
                        context,
                        getYearForIndex(
                            currentIndex
                        ),
                        getMonthForIndex(
                            currentIndex
                        ),
                        availabilityCallback,
                        {
                            callback(currentIndex - 1)
                        }
                    )
                } else {
                    callback(currentIndex)
                }
            }

            isDataForDateAvailable(
                context,
                STARTING_YEAR,
                STARTING_MONTH,
                availabilityCallback,
                {
                    callback(currentIndex)
                })
        } else {
            if (allData.isEmpty()) {
                errorCallback(NetworkNotAvailableException())
            }
        }
    }

    private fun isDataForDateAvailable(
        context: Context,
        year: Int,
        month: Int,
        callback: (Boolean) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        val localData: CompanyPaymentData? = FileStorage.readData(context, year, month)

        if (localData != null) {
            callback(true)
        } else {
            if (NetworkUtil.isNetworkConnected(
                    context
                )
            ) {
                Api.INSTANCE.getCompanyPaymentData(
                    CompanyPaymentDataRequest(year, month, 1)
                ).enqueue({ _, response ->
                    callback(response.body()?.totalCount?.let { it > 0 } ?: false)
                }, { _, t ->
                    errorCallback(t)
                })
            } else {
                errorCallback(NetworkNotAvailableException())
            }
        }
    }

    fun removeData(context: Context, year: Int, month: Int) {
        FileStorage.storeAllData(context, FileStorage.readAllData(context).apply {
            get(year)?.remove(month)
        })
    }

    fun getData(
        context: Context,
        year: Int,
        month: Int,
        loadCallback: (CompanyPaymentData, LoadSource) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        FileStorage.readData(context, year, month)?.let { localData ->
            loadCallback(localData,
                LoadSource.LOCAL_DATA
            )

            if (NetworkUtil.isNetworkConnected(
                    context
                )
            ) {
                Api.INSTANCE.getCompanyPaymentData(
                    CompanyPaymentDataRequest(year, month, 1)
                ).enqueue({ _, response ->
                    response.body()?.let { data ->
                        if (data.totalCount != localData.totalCount) {
                            removeData(
                                context,
                                year,
                                month
                            )
                            getData(
                                context,
                                year,
                                month,
                                loadCallback,
                                errorCallback
                            )
                        }
                    }
                }, { _, t ->
                    errorCallback(t)
                })
            }
        } ?: run {
            if (NetworkUtil.isNetworkConnected(
                    context
                )
            ) {
                // First we do a preload, so that we can show the user the first 1000 items and then silently in the background load the complete data
                Api.INSTANCE.getCompanyPaymentData(
                    CompanyPaymentDataRequest(year, month, pageSize = 1000)
                ).enqueue({ _, response ->
                    response.body()?.let { data ->
                        loadCallback(data,
                            LoadSource.NETWORK_PRELOAD
                        )

                        Api.INSTANCE.getCompanyPaymentData(
                            CompanyPaymentDataRequest(year, month)
                        ).enqueue({ _, response ->
                            response.body()?.let { data ->
                                FileStorage.storeData(context, data, year, month)
                                loadCallback(data,
                                    LoadSource.NETWORK
                                )
                            }
                        }, { _, t ->
                            errorCallback(t)
                        })
                    }
                }, { _, t ->
                    errorCallback(t)
                })
            } else {
                errorCallback(NetworkNotAvailableException())
            }
        }
    }

    class NetworkNotAvailableException : IllegalStateException()

    enum class LoadSource {
        NETWORK_PRELOAD,
        NETWORK,
        LOCAL_DATA
    }

    fun getYearForIndex(index: Int): Int {
        return STARTING_YEAR + ((STARTING_MONTH + index - 1) / 12)
    }

    fun getMonthForIndex(index: Int): Int {
        return ((STARTING_MONTH + index - 1) % 12) + 1
    }

    const val STARTING_YEAR: Int = 2020
    const val STARTING_MONTH: Int = 3
}