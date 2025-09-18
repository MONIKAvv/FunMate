package vv.monika.funMaatee.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vv.monika.funMaatee.databinding.GameItemBinding
import vv.monika.funMaatee.model.GameItem

class GameAdapter( private val items: List<GameItem>,
                   private val onClick: (GameItem) -> Unit): RecyclerView.Adapter<GameAdapter.GameViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameViewHolder {

        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GameViewHolder,
        position: Int
    ) {
       holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class GameViewHolder(private val binding: GameItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: GameItem) {
            binding.gameTitle.text = item.title
            // load local drawable
            binding.gameImage.setImageResource(item.iconRes)
            // If you use URL images with Coil:
            // binding.ivIcon.load(item.iconUrl)

            binding.root.setOnClickListener { onClick(item) }
        }

    }
}


