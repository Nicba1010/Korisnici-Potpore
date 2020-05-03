package dev.banic.korisnicipotpore.ui.list

import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import dev.banic.korisnicipotpore.data.Api.CompanyPaymentData.Company
import dev.banic.korisnicipotpore.R
import dev.banic.korisnicipotpore.ui.list.CompanyAdapter.Companion.SortKey.*
import kotlinx.android.synthetic.main.item_company.view.*

class CompanyAdapter(
    private var data: MutableList<Company>,
    private var filteredData: MutableList<Company> = data
) : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>(), Filterable {
    private var sortKeyDirty: Boolean = false
    var sortKey: SortKey = NAME
        set(value) {
            if (field == value) return
            sortKeyDirty = true
            field = value
        }

    private var sortDirectionDirty: Boolean = false
    var sortAscending: Boolean = true
        set(value) {
            if (field == value) return
            sortDirectionDirty = true
            field = value
        }

    private val comparator
        get() = compareBy<Company> {
            when (sortKey) {
                NAME -> it.name
                PIN -> it.oib
                SUPPORTED_WORKERS -> it.supportedEmployees
                PAID_OUT_SUPPORT -> it.paidOutSupport
            }
        }.let {
            if (sortAscending) it else it.reversed()
        }

    fun notifySortChanged() {
        if ((sortDirectionDirty and sortKeyDirty) or (!sortDirectionDirty and sortKeyDirty)) {
            data.sortWith(comparator)
            filteredData.sortWith(comparator)
        } else if (sortDirectionDirty && !sortKeyDirty) {
            data = data.reversed().toMutableList()
            filteredData = filteredData.reversed().toMutableList()
        } else {
            sortKeyDirty = false
            sortDirectionDirty = false

            return
        }

        sortKeyDirty = false
        sortDirectionDirty = false

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_company,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(filteredData[position])
    }

    override fun getFilter(): Filter = object : Filter() {
        private var lastQuery: CharSequence? = null

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val continuationOfLastQuery = lastQuery?.let {
                constraint.startsWith(it)
            } ?: false
            lastQuery = constraint

            return FilterResults().apply {
                values = (constraint.toString().takeIf {
                    it.isNotEmpty()
                }?.let {
                    (if (continuationOfLastQuery) filteredData else data).filter {
                        it.name.contains(constraint, ignoreCase = true) or
                                it.oib.contains(constraint, ignoreCase = true)
                    }
                } ?: data)
            }
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            @Suppress("UNCHECKED_CAST")
            (results.values as? List<Company>)?.let {
                filteredData = it.toMutableList()
            }
            notifyDataSetChanged()
        }
    }

    fun updateData(data: MutableList<Company>) {
        sortKeyDirty = true
        sortDirectionDirty = true

        this.data = data
        this.filteredData = data
        notifySortChanged()
    }

    class CompanyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(company: Company) {
            itemView.tv_company_name.text = company.name
            itemView.tv_company_oib.text = itemView.context.getString(
                R.string.oib,
                company.oib
            )
            itemView.tv_company_supported_employees.text = itemView.context.getString(
                R.string.supported_employees,
                company.supportedEmployees
            )
            itemView.tv_company_support_paid_out.text = itemView.context.getString(
                R.string.paid_out_support,
                FORMATTER.format(company.paidOutSupport)
            )
        }

        companion object {
            val FORMATTER: DecimalFormat = DecimalFormat("###,###,###,###.##")
        }
    }

    companion object {
        enum class SortKey {
            NAME, PIN, SUPPORTED_WORKERS, PAID_OUT_SUPPORT
        }
    }
}