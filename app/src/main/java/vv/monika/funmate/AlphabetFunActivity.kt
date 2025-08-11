package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vv.monika.funmate.databinding.ActivityAlphabetFunBinding

class AlphabetFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlphabetFunBinding
    private var isHintVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlphabetFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHint.setOnClickListener {
            toggleHint()

        }
binding.hintBubble.visibility = View.GONE

        binding.root.setOnClickListener {
            binding.hintBubble.visibility = View.GONE
            hideHint()
            isHintVisible = !isHintVisible
        }

    }

    private fun toggleHint() {
        if (isHintVisible){
            hideHint()
        }else{
            showHint()
        }
        isHintVisible = !isHintVisible
    }

    private fun showHint() {
        binding.btnHint.setImageResource(R.drawable.close_hint)
        binding.hintBubble.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = 20f
            animate().alpha(1f).translationY(0f).setDuration(180).start()
        }
    }

    private fun hideHint() {
        binding.btnHint.setImageResource(R.drawable.hint_icon)
        binding.hintBubble.animate()
            .alpha(0f)
            .translationY(20f)
            .setDuration(180)
            .withEndAction { binding.hintBubble.visibility = View.GONE }
            .start()

    }
}