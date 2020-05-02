package dev.banic.korisnicipotpore

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    @POST("GetCompanyPaymentData")
    fun getCompanyPaymentData(@Body body: CompanyPaymentDataRequest): Call<CompanyPaymentData>

    data class CompanyPaymentDataRequest(
        val pageSize: Int = DEFAULT_PAGE_SIZE,
        val pageIndex: Int = 1,
        val month: Int
    )

    data class CompanyPaymentData(
        @SerializedName("Data")
        val data: MutableList<Company>,
        @SerializedName("TotalCount")
        val totalCount: Int
    ) {
        data class Company(
            @SerializedName("EmployerName")
            val name: String,
            @SerializedName("Oib")
            val oib: String,
            @SerializedName("SupportedEmployeeNumber")
            val supportedEmployees: Int,
            @SerializedName("SupportPaidAmount")
            val paidOutSupport: Float
        )
    }

    companion object {
        const val DEFAULT_PAGE_SIZE: Int = 1000000000

        private const val BASE_URL: String = "https://mjera-orm.hzz.hr/api/"
        private val GSON: Gson = GsonBuilder().create()
        private val GSON_CONVERTER_FACTORY: GsonConverterFactory = GsonConverterFactory.create(GSON)
        private val RETROFIT: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GSON_CONVERTER_FACTORY)
            .build()
        val INSTANCE: Api = RETROFIT.create(Api::class.java)
    }
}