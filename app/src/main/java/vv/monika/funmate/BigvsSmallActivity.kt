package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.databinding.ActivityBigvsSmallBinding

class BigvsSmallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBigvsSmallBinding
val correctOption = "C"
    var selectedOption : String? = null

    private var isHintVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBigvsSmallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.vsOptA.setOnClickListener {
            selectedOption = "A"
            checkAnswer()
        }
        binding.vsOptB.setOnClickListener {
            selectedOption = "B"
            checkAnswer()
        }
        binding.vsOptC.setOnClickListener {
            selectedOption = "C"
            checkAnswer()
        }
        binding.vsOptD.setOnClickListener {
            selectedOption = "D"
            checkAnswer()
        }

        binding.btnHint.setOnClickListener {
            toggleHint()
        }
        // Make sure hintBubble refers to the hint layout in the same screen
        binding.hintBubble.visibility = View.GONE

        binding.root.setOnClickListener {
            binding.hintBubble.visibility = View.GONE
            hideHint()
            isHintVisible = !isHintVisible
        }

    }

    private fun checkAnswer() {
        if (selectedOption == correctOption){
            CustomAlert.showCustomAlert(this, AlertType.CORRECT, "Good", "Move to next")
        }else{
            CustomAlert.showCustomAlert(this, AlertType.WRONG, "Oops Wrong", "Please once more")
        }
    }

    private fun showHint() {
        binding.btnHint.setImageResource(R.drawable.close_hint) // optional, can change icon
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

    private fun toggleHint() {
        if (isHintVisible) {
            hideHint()
        } else {
            showHint()
        }
        isHintVisible = !isHintVisible
    }
}
