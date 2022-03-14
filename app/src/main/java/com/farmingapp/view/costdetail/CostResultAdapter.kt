package com.farmingapp.view.costdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farmingapp.databinding.ItemCostResultBinding
import com.farmingapp.model.CostModel

class CostResultAdapter(
    private val dataset: List<CostModel>,
    private val editClickListener: OnEditClickListener
): RecyclerView.Adapter<CostResultAdapter.CostResultViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CostResultAdapter.CostResultViewHolder {
        val binding = ItemCostResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CostResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CostResultViewHolder, position: Int) {
        with(holder) {
            with(dataset[position]) {
                bind(this)
            }
        }
    }

    override fun getItemCount(): Int = dataset.size

    inner class CostResultViewHolder(private val binding: ItemCostResultBinding)
        :RecyclerView.ViewHolder(binding.root) {

            fun bind(model: CostModel) {
                binding.tvTitle.text = model.title
                binding.tvSubtitle.text = model.value
                binding.tvLeftValue.text = model.quantity
                binding.tvMiddleValue.text = model.rate
                binding.tvRightValue.text = model.amount

                binding.ivEdit.setOnClickListener {
                    editClickListener.onEditClick(model)
                }
            }
        }
}