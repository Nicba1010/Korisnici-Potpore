package dev.banic.korisnicipotpore

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.banic.korisnicipotpore.Api.CompanyPaymentData
import dev.banic.korisnicipotpore.CompanyAdapter.Companion.SortKey
import dev.banic.korisnicipotpore.SortDialogFragment.OnSortChosenListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MainActivity : AppCompatActivity(), OnSortChosenListener {
    lateinit var companyAdapter: CompanyAdapter
    lateinit var searchView: SearchView
    private val sp: SharedPreferences by lazy {
        getPreferences(Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        showInstructionsScreen()

        companyAdapter = CompanyAdapter(mutableListOf())
        rv_data.layoutManager = LinearLayoutManager(this)
        rv_data.adapter = companyAdapter

        srl_data.setOnRefreshListener {
            loadPartialThenWhole()
        }

        FileStorage.readData(this)?.let {
            companyAdapter.updateData(it.data)
        } ?: run {
            loadPartialThenWhole()
        }

        fab.setOnClickListener {
            SortDialogFragment.newInstance(
                companyAdapter.sortKey,
                companyAdapter.sortAscending
            ).show(supportFragmentManager, null)
        }
    }

    private fun showInstructionsScreen() {
        if (!sp.getBoolean(INSTRUCTIONS_SHOWN_PREFERENCE, false)) {
            sp.edit().putBoolean(INSTRUCTIONS_SHOWN_PREFERENCE, true).apply()
            fab.isEnabled = false
            root_dim.setOnClickListener {
                root_frame.removeView(root_dim)
                invalidateOptionsMenu()
                fab.isEnabled = true
            }
        } else {
            root_frame.removeView(root_dim)
        }
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

    private fun loadPartialThenWhole() {
        srl_data.isRefreshing = true
        getCompanyPaymentData(size = 1000) {
            srl_data.isRefreshing = false

            Snackbar.make(
                root,
                getString(R.string.data_partially_loaded),
                Snackbar.LENGTH_INDEFINITE
            ).addCallback(DotLoadingSnackbarCallback()).show()

            getCompanyPaymentData {
                Snackbar.make(
                    root,
                    "Podatci potpuno učitani",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getCompanyPaymentData(
        size: Int = Api.DEFAULT_PAGE_SIZE,
        runAfter: (() -> Unit)? = null
    ) {
        Api.INSTANCE.getCompanyPaymentData(
            Api.CompanyPaymentDataRequest(pageSize = size, month = 3)
        ).enqueue(object : Callback<CompanyPaymentData> {
            override fun onResponse(
                call: Call<CompanyPaymentData>,
                response: Response<CompanyPaymentData>
            ) {
                response.body()?.let { data ->
                    Timber.d("Loaded ${data.data.size} items...")
                    companyAdapter.updateData(data.data)
                    runAfter?.invoke()

                    if (size >= Api.DEFAULT_PAGE_SIZE) {
                        FileStorage.storeData(this@MainActivity, data)
                    }
                }
            }

            override fun onFailure(call: Call<CompanyPaymentData>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            maxWidth = Int.MAX_VALUE

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    companyAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    companyAdapter.filter.filter(query)
                    return true
                }
            })
        }

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchView.setSearchButtonEnabled(fab.isEnabled)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val INSTRUCTIONS_SHOWN_PREFERENCE = "MainActivity.INSTRUCTIONS_SHOWN"
    }
}
