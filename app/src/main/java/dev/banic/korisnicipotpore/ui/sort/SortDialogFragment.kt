package dev.banic.korisnicipotpore.ui.sort

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import dev.banic.korisnicipotpore.ui.list.CompanyAdapter.Companion.SortKey
import dev.banic.korisnicipotpore.R
import kotlinx.android.synthetic.main.dialog_sort.view.*


class SortDialogFragment : DialogFragment() {
    private lateinit var sortKey: SortKey
    private var sortAscending: Boolean = true

    var onSortChosenListener: OnSortChosenListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sortKey = (arguments?.getSerializable(SORT_KEY_ARGUMENT) as? SortKey)
            ?: throw IllegalStateException(
                "SortDialogFragment initialized without SORT_KEY_ARGUMENT"
            )
        sortAscending = arguments?.getBoolean(SORT_ASCENDING_ARGUMENT)
            ?: throw IllegalStateException(
                "SortDialogFragment initialized without SORT_ASCENDING_ARGUMENT"
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_sort, container, false).apply {
            prepareImageView(iv_name_up, SortKey.NAME, true)
            prepareImageView(iv_name_down, SortKey.NAME, false)
            prepareImageView(iv_pin_up, SortKey.PIN, true)
            prepareImageView(iv_pin_down, SortKey.PIN, false)
            prepareImageView(iv_workers_up, SortKey.SUPPORTED_WORKERS, true)
            prepareImageView(iv_workers_down, SortKey.SUPPORTED_WORKERS, false)
            prepareImageView(iv_paid_out_up, SortKey.PAID_OUT_SUPPORT, true)
            prepareImageView(iv_paid_out_down, SortKey.PAID_OUT_SUPPORT, false)
        }
    }

    private fun prepareImageView(
        imageView: ImageView,
        sortKey: SortKey,
        sortAscending: Boolean
    ) {
        imageView.let { iv ->
            if (this.sortKey == sortKey && this.sortAscending == sortAscending) {
                iv.imageTintList = ColorStateList.valueOf(
                    requireContext().getColor(R.color.colorPrimary)
                )
            }
            iv.setOnClickListener {
                onSortChosenListener?.onSortChosen(sortKey, sortAscending)
                dismiss()
            }
        }
    }

    interface OnSortChosenListener {
        fun onSortChosen(sortKey: SortKey, sortAscending: Boolean)
    }

    companion object {
        const val SORT_KEY_ARGUMENT: String = "SortDialogFragment.SORT_KEY"
        const val SORT_ASCENDING_ARGUMENT: String = "SortDialogFragment.SORT_ASCENDING"

        fun newInstance(
            currentSortKey: SortKey,
            currentSortAscending: Boolean
        ): SortDialogFragment {
            return SortDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SORT_KEY_ARGUMENT, currentSortKey)
                    putBoolean(SORT_ASCENDING_ARGUMENT, currentSortAscending)
                }
            }
        }
    }
}