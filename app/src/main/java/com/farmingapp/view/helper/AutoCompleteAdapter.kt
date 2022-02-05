package com.farmingapp.view.helper

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.farmingapp.R

class AutoCompleteAdapter(
    private val mContext: Context,
    items: ArrayList<String>
) : ArrayAdapter<String?>(mContext, 0, items.toList()) {

    private val itemsAll = items.clone() as ArrayList<*>
    private var suggestions = ArrayList<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v: View? = convertView
        if (v == null) {
            val vi = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(R.layout.item_selection, null)
        }

        return v!!
    }

    override fun getFilter(): Filter {
        return nameFilter
    }

    private var nameFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String {
            return (resultValue as String)
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                suggestions.clear()
                for (item in itemsAll) {
                    if ((item as String).lowercase().startsWith(constraint.toString().lowercase())) {
                        suggestions.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            try {
                val filteredList =  results?.values as ArrayList<*>?

                if (results != null && results.count > 0) {
                    clear()
                    for (c: Any in filteredList ?: listOf<String>()) {
                        add(c as String)
                    }
                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("AutoCompleteAdapter", e.message.orEmpty())
            }
        }
    }
}