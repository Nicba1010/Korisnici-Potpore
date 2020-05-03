package dev.banic.korisnicipotpore.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.banic.korisnicipotpore.R
import dev.banic.korisnicipotpore.ui.ActivityFloatingActionButtonClickListener
import dev.banic.korisnicipotpore.ui.FilterViewModel
import dev.banic.korisnicipotpore.ui.list.CompanyAdapter.Companion.SortKey
import dev.banic.korisnicipotpore.ui.sort.SortDialogFragment
import dev.banic.korisnicipotpore.ui.sort.SortDialogFragment.OnSortChosenListener
import dev.banic.korisnicipotpore.usecases.data.api.local.RemoveLocalApiDataForYearMonthUseCase
import dev.banic.korisnicipotpore.usecases.data.api.unified.GetApiDataForYearMonthUseCase.LoadSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import timber.log.Timber
import kotlin.properties.Delegates

class CompanyListFragment : Fragment(), ActivityFloatingActionButtonClickListener,
    OnSortChosenListener {
    private var snackbar: Snackbar? = null

    private val filterViewModel: FilterViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
    }

    lateinit var removeLocalApiDataForYearMonthUseCase: RemoveLocalApiDataForYearMonthUseCase

    private var year by Delegates.notNull<Int>()
    private var month by Delegates.notNull<Int>()
    private lateinit var companyAdapter: CompanyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        year = arguments?.getInt(YEAR_ARGUMENT)
            ?: throw IllegalStateException(
                "CompanyListFragment initialized without YEAR_ARGUMENT"
            )
        month = arguments?.getInt(MONTH_ARGUMENT)
            ?: throw IllegalStateException(
                "CompanyListFragment initialized without MONTH_ARGUMENT"
            )

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        companyAdapter = CompanyAdapter()
        rv_data.layoutManager = LinearLayoutManager(requireContext())
        rv_data.adapter = companyAdapter

        srl_data.setOnRefreshListener {
            removeLocalApiDataForYearMonthUseCase.execute(year, month)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadData(view) {
                        srl_data.isRefreshing = false
                    }
                }, {
                    Timber.e(it)
                })
        }

        filterViewModel.filter.observe(viewLifecycleOwner, Observer {
            companyAdapter.filter.filter(it)
        })

        loadData(view)
    }

    override fun onActivityFloatingActionButtonClick(view: View) {
        SortDialogFragment.newInstance(
            companyAdapter.sortKey,
            companyAdapter.sortAscending
        ).apply {
            onSortChosenListener = this@CompanyListFragment
        }.show(childFragmentManager, null)
    }

    override fun onSortChosen(sortKey: SortKey, sortAscending: Boolean) {
        companyAdapter.apply {
            this.sortKey = sortKey
            this.sortAscending = sortAscending
            notifySortChanged()

            Snackbar.make(
                root,
                getString(
                    R.string.sorted_by_direction_sort_key,
                    getString(if (sortAscending) R.string.ascending else R.string.descending),
                    getString(
                        when (sortKey) {
                            SortKey.NAME -> R.string.by_name
                            SortKey.PIN -> R.string.by_pin
                            SortKey.SUPPORTED_WORKERS -> R.string.by_num_of_workers
                            SortKey.PAID_OUT_SUPPORT -> R.string.by_paid_out_support
                        }
                    )
                ),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadData(view: View, onDataLoaded: (() -> Unit)? = null) {
        var lastLoadSource: LoadSource? = null
        var preloadSnackbar: Snackbar? = null

//        DataStorageHandler.getData(
//            requireContext(), year, month,
//            { it, loadSource ->
//                when (loadSource) {
//                    LoadSource.NETWORK_PRELOAD -> {
//                        preloadSnackbar = Snackbar.make(
//                            root,
//                            getString(R.string.data_partially_loaded),
//                            Snackbar.LENGTH_INDEFINITE
//                        ).setNonSwipeable().animateLoading().also {
//                            it.show()
//                        }
//                    }
//                    LoadSource.NETWORK -> {
//                        when (lastLoadSource) {
//                            LoadSource.NETWORK_PRELOAD -> {
//                                preloadSnackbar?.dismiss()
//                                Snackbar.make(
//                                    root,
//                                    getString(R.string.data_completely_loaded),
//                                    Snackbar.LENGTH_LONG
//                                ).show()
//                            }
//                            LoadSource.NETWORK -> {
//                            }
//                            LoadSource.LOCAL_DATA -> {
//                                Snackbar.make(
//                                    root,
//                                    getString(R.string.local_data_updated),
//                                    Snackbar.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    }
//                    LoadSource.LOCAL_DATA -> {
//                    }
//                }
//                lastLoadSource = loadSource
//                onDataLoaded?.invoke()
//                companyAdapter.updateData(it.data)
//            }, { t ->
//                preloadSnackbar?.dismiss()
//                if (t is NetworkNotAvailableException) {
//                    requireActivity().makeNetworkCheckSnackbar(
//                        root,
//                        R.string.please_enable_network,
//                        R.string.please_enable_network_manually,
//                        R.string.trying_to_connect,
//                        Snackbar.LENGTH_INDEFINITE
//                    ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
//                        override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
//                            loadData(view)
//                        }
//                    }).show()
//                } else {
//                    snackbar = Snackbar.make(
//                        view.root,
//                        R.string.unexpected_error_loading_data,
//                        Snackbar.LENGTH_SHORT
//                    ).also {
//                        it.show()
//                    }
//                }
//            }
//        )
    }

    override fun onPause() {
        snackbar?.dismiss()
        super.onPause()
    }

    companion object {
        const val YEAR_ARGUMENT: String = "CompanyListFragment.YEAR"
        const val MONTH_ARGUMENT: String = "CompanyListFragment.MONTH"

        fun newInstance(
            year: Int,
            month: Int
        ): CompanyListFragment {
            return CompanyListFragment().apply {
                arguments = Bundle().apply {
                    putInt(YEAR_ARGUMENT, year)
                    putInt(MONTH_ARGUMENT, month)
                }
            }
        }
    }
}
