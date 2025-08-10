package vv.monika.funmate

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import vv.monika.funmate.adapter.GameAdapter
import vv.monika.funmate.databinding.ActivityGameBinding
import vv.monika.funmate.model.GameItem

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

//dummy data
        val data = listOf(
            GameItem(1, "Stumble Guys", R.drawable.gameimage1),
            GameItem(2, "Minecraft", R.drawable.minecraft),
            GameItem(3, "Stumble Guys", R.drawable.gameimage1),
            GameItem(4, "Minecraft", R.drawable.minecraft),
            GameItem(5, "Stumble Guys", R.drawable.gameimage1),
            GameItem(6, "Minecraft", R.drawable.minecraft),
            GameItem(7, "Stumble Guys", R.drawable.gameimage1),
            GameItem(8, "Minecraft", R.drawable.minecraft),

        )
        val spanCount = 2
        val spacingPx =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // define in dimens.xml or use dp->px

        val layoutManager = GridLayoutManager(this, spanCount)
        binding.gameRecyclerView.layoutManager = layoutManager
        binding.gameRecyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacingPx, true))
        binding.gameRecyclerView.adapter = GameAdapter(data) { item ->
            Toast.makeText(this, "Clicked ${item.title}", Toast.LENGTH_SHORT).show()
//             open game / start activity / show more info
        }


        binding.backButton.setOnClickListener {
            finish()
        }
    }
}