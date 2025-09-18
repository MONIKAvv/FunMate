package vv.monika.funMaatee.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import vv.monika.funMaatee.databinding.HistoryItemsBinding

class HistoryAdapter(
    private val withDrawStatus: List<String>,
    private val totalCoin: List<String>,
    private val dataAndTime: List<String>,
//    private val couponCode: MutableList<String>
) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryViewHolder {
        val viewBinding =
            HistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(viewBinding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return withDrawStatus.size
    }

    inner class HistoryViewHolder(private val binding: HistoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(position: Int) {
            binding.apply {

                status.text = withDrawStatus[position]
                noOfCoins.text = totalCoin[position]
              enterDateTime.text = dataAndTime[position]

//                change color of text
                when(withDrawStatus[position].lowercase()){
                    "success" -> status.setTextColor(
                        binding.root.context.getColor(android.R.color.holo_green_dark)
                    )
                    "pending" -> status.setTextColor(
                        binding.root.context.getColor(android.R.color.holo_orange_dark)
                    )
                    "failed" -> status.setTextColor(
                        binding.root.context.getColor(android.R.color.holo_red_dark)
                    )

                }

            }
        }

    }
}