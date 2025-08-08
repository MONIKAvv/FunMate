package vv.monika.funmate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import vv.monika.funmate.databinding.FragmentRecentBinding
import vv.monika.funmate.databinding.HistoryItemsBinding
import java.util.Date

class HistoryAdapter(
    private val withDrawStatus: MutableList<String>,
    private val totalCoin: MutableList<Number>,
    private val dataAndTime: MutableList<Date>,
    private val couponCode: MutableList<String>
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

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return withDrawStatus.size
    }

    inner class HistoryViewHolder(private val binding: HistoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                status.text = withDrawStatus[position]
                noOfCoins.text = totalCoin[position] as CharSequence?


            }
        }

    }
}