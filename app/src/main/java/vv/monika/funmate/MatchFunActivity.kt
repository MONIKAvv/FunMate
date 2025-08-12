package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.CutCornerShape
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vv.monika.funmate.databinding.ActivityMatchFunBinding

class MatchFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchFunBinding
    private var isHintVisible = false

    val correctOption = "A"
    var selectedOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMatchFunBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.mathOptA.setOnClickListener {
            selectedOption = "A"
            checkAnswer()
        }
        binding.mathOptB.setOnClickListener {
            selectedOption = "B"
            checkAnswer()
        }
        binding.mathOptC.setOnClickListener {
            selectedOption = "C"
            checkAnswer()
        }
        binding.mathOptD.setOnClickListener {
            selectedOption = "D"
            checkAnswer()
        }


        binding.hintBubble.visibility = View.GONE
        binding.root.setOnClickListener {
            binding.hintBubble.visibility = View.GONE
            hideHint()
            isHintVisible = !isHintVisible
        }

        binding.btnHint.setOnClickListener {
            toggleHint()
        }


    }

    private fun checkAnswer() {
        if (selectedOption == correctOption){
            CustomAlert.showCustomAlert(this, AlertType.CORRECT, "Correct ","oky well done")
        }else{
            CustomAlert.showCustomAlert(this, AlertType.WRONG, "Wrong", "Please do it corectly")
        }
    }

    private fun toggleHint() {
        if (isHintVisible) {
            hideHint()
        } else {
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
            .setDuration(180).withEndAction { binding.hintBubble.visibility = View.GONE }
            .start()
    }

}