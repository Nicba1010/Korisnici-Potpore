package dev.banic.korisnicipotpore.ui

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.banic.korisnicipotpore.R
import dev.banic.korisnicipotpore.ui.list.CompanyListsPager
import dev.banic.korisnicipotpore.usecases.data.api.unified.GetApiListCountUseCase
import dev.banic.korisnicipotpore.util.makeNetworkCheckSnackbar
import dev.banic.korisnicipotpore.util.setSearchButtonEnabled
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.overlay_instructions.*


class CompanyListsActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private val filterViewModel: FilterViewModel by viewModels()
    private val sp: SharedPreferences by lazy {
        getPreferences(Context.MODE_PRIVATE)
    }

    lateinit var getApiListCountUseCase: GetApiListCountUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        prepareInstructionsScreen()
    }

    private fun prepareInstructionsScreen() {
        if (!sp.getBoolean(INSTRUCTIONS_SHOWN_PREFERENCE, false)) {
            fab.isEnabled = false

            root_dim.setOnClickListener {
                sp.edit().putBoolean(INSTRUCTIONS_SHOWN_PREFERENCE, true).apply()
                root_frame.removeView(root_dim)
                invalidateOptionsMenu()
                fab.isEnabled = true
                prepareOtherViews()
            }
        } else {
            root_frame.removeView(root_dim)
            prepareOtherViews()
        }
    }

    private fun prepareOtherViews() {
        vp_lists.adapter = CompanyListsPager(this, getApiListCountUseCase) {
            makeNetworkCheckSnackbar(
                root_frame,
                R.string.please_enable_network,
                R.string.please_enable_network_manually,
                R.string.trying_to_connect,
                Snackbar.LENGTH_INDEFINITE
            ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                    (vp_lists.adapter as? CompanyListsPager)?.requestListCountRefresh()
                }
            }).show()
        }

        fab.setOnClickListener {
            supportFragmentManager.fragments.mapNotNull { fragment ->
                fragment as? ActivityFloatingActionButtonClickListener
            }.forEach { fragment ->
                fragment.onActivityFloatingActionButtonClick(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            maxWidth = Int.MAX_VALUE

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    filterViewModel.filter.value = query
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    filterViewModel.filter.value = query
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
        const val INSTRUCTIONS_SHOWN_PREFERENCE = "CompanyListsActivity.INSTRUCTIONS_SHOWN"
        const val REQUEST_CODE_CONNECTIVITY = 0x01
    }
}
