package com.farmingapp.view.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmingapp.databinding.ItemActionBinding
import com.farmingapp.databinding.ItemResultBinding
import com.farmingapp.databinding.ItemSelectionBinding
import com.farmingapp.model.GenericResultModel

class ResultAdapter(
    private val dataSet: List<GenericResultModel>,
    private val nextListener: OnNextListener
): RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_KEY_VALUE -> {
                val binding = ItemResultBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                KeyValueViewHolder(binding)
            }
            TYPE_INFO -> {
                val binding = ItemSelectionBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ResultViewHolder(binding)
            }
            else -> {
                val binding = ItemActionBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ActionViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(dataSet[position]) {
            when (holder.itemViewType) {
                TYPE_KEY_VALUE -> {
                    (holder as KeyValueViewHolder).bind(this)
                }
                TYPE_INFO -> {
                    (holder as ResultViewHolder).bind(this)
                }
                else -> {
                    (holder as ActionViewHolder).bind()
                }
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].key) {
            "INFO" -> {
                TYPE_INFO
            }
            "ACTION" -> {
                TYPE_ACTION
            }
            else -> {
                TYPE_KEY_VALUE
            }
        }
    }

    open class ViewHolder(v: View?) : RecyclerView.ViewHolder((v)!!)

    inner class ResultViewHolder(private val binding: ItemSelectionBinding)
        : ViewHolder(binding.root) {

        fun bind(model: GenericResultModel) {
            binding.tvValue.text = model.value
        }
    }

    inner class KeyValueViewHolder(private val binding: ItemResultBinding)
        : ViewHolder(binding.root) {

        fun bind(model: GenericResultModel) {
            binding.tvLabel.text = model.label
            binding.tvValue.text = model.value
        }
    }

    inner class ActionViewHolder(private val binding: ItemActionBinding)
        : ViewHolder(binding.root) {

            fun bind() {
                binding.btnNext.setOnClickListener {
                    nextListener.next()
                }
            }
        }

    companion object {
        private const val TYPE_KEY_VALUE = 0
        private const val TYPE_INFO = 1
        private const val TYPE_ACTION = 2
    }
}