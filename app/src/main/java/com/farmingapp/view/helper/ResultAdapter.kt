package com.farmingapp.view.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmingapp.databinding.ItemResultBinding
import com.farmingapp.model.GenericResultModel

class ResultAdapter(
    private val dataSet: List<GenericResultModel>
): RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        with(holder) {
            with(dataSet[position]) {
                bind(this)
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ResultViewHolder(private val binding: ItemResultBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GenericResultModel) {
            binding.tvLabel.text = model.label
            binding.tvValue.text = model.value
        }
    }
}