package vv.monika.funmate

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.interaction.DragInteraction
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
        binding.backButton.setOnClickListener {
            finish()
        }

//dummy data
        val data = listOf(
            GameItem(1, "Stumble Guys", R.drawable.gameimage1, "https://www.stumbleguys.com/"),
            GameItem(2, "Minecraft", R.drawable.minecraft, "https://www.minecraft.net/"),
            GameItem(3, "Free Fire", R.drawable.gameimage1, "https://ff.garena.com/"),
            GameItem(4, "Among Us", R.drawable.minecraft, "https://innersloth.com/gameAmongUs.php"),

            GameItem(5, "Minecraft", R.drawable.minecraft, "https://www.minecraft.net/"),
            GameItem(6, "Free Fire", R.drawable.gameimage1, "https://ff.garena.com/"),
            GameItem(7, "Among Us", R.drawable.minecraft, "https://innersloth.com/gameAmongUs.php"),
            GameItem(8, "Stumble Guys", R.drawable.gameimage1, "https://www.stumbleguys.com/"),


            )
        val spanCount = 2
        val spacingPx =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // define in dimens.xml or use dp->px

        val layoutManager = GridLayoutManager(this, spanCount)
        binding.gameRecyclerView.layoutManager = layoutManager
        binding.gameRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacingPx,
                true
            )
        )
        binding.gameRecyclerView.adapter = GameAdapter(data) { item ->
            Toast.makeText(this, "Clicked ${item.title}", Toast.LENGTH_SHORT).show()
//             open game / start activity / show more info custom tab open

            Congrats.showCongratsAlert(
                this,
                onClaimClick = { openCustomTab(item.url)},
                30000
            )

        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun openCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}