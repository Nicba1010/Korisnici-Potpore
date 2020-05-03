package dev.banic.korisnicipotpore.ui.list

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.banic.korisnicipotpore.usecases.data.api.unified.GetApiListCountUseCase
import dev.banic.korisnicipotpore.util.ApiUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class CompanyListsPager(
    activity: FragmentActivity,
    private val getApiListCountUseCase: GetApiListCountUseCase,
    private val networkNotAvailableCallback: () -> Unit
) : FragmentStatePagerAdapter(
    activity.supportFragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val context: Context = activity.applicationContext
    private var currentCount: Int = 0
        set(value) {
            if (field == value) return

            field = value
            notifyDataSetChanged()
        }

    private val monthYearFormat: SimpleDateFormat = SimpleDateFormat(
        "MMMM yyyy",
        context.resources.configuration.locales[0]
    )

    init {
        requestListCountRefresh()
    }

    fun requestListCountRefresh() {
        getApiListCountUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                currentCount = it
            }, {
                networkNotAvailableCallback()
            })
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return monthYearFormat.format(
            Calendar.getInstance().apply {
                set(
                    ApiUtil.getYearForIndex(position),
                    ApiUtil.getMonthForIndex(position),
                    1
                )
            }.time
        )
    }

    override fun getItem(position: Int): Fragment {
        return CompanyListFragment.newInstance(
            ApiUtil.getYearForIndex(position),
            ApiUtil.getMonthForIndex(position)
        )
    }

    override fun getCount(): Int {
        return currentCount
    }
}