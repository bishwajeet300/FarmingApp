package com.farmingapp.view.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmingapp.databinding.ItemSelectionBinding
import com.farmingapp.model.GenericOptionModel
import com.farmingapp.model.OptionsType

class OptionsAdapter(
    private val type: OptionsType,
    private val dataset: List<GenericOptionModel>,
    private val optionsClickListener: OnOptionsClickListener
): RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionsAdapter.OptionsViewHolder {
        val binding = ItemSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        with(holder) {
            with(dataset[position]) {
                bind(this)
            }
        }
    }

    override fun getItemCount(): Int = dataset.size

    inner class OptionsViewHolder(private val binding: ItemSelectionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: GenericOptionModel) {
            binding.tvValue.text = model.label

            binding.tvValue.setOnClickListener {
                optionsClickListener.onOptionsClick(type, model)
            }
        }
    }
}