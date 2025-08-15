package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.databinding.ActivityAlphabetFunBinding

class AlphabetFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlphabetFunBinding
    private var isHintVisible = false

    val correctOption = "C"
    var selectedOption: String? = null // store user choice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlphabetFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

//        swtitch case for question matching



        binding.alphabetOptA.setOnClickListener {
            selectedOption = "A"
            checkAnswer()
        }

        binding.alphabetOptB.setOnClickListener {
            selectedOption = "B"
            checkAnswer()
        }

        binding.alphabetOptC.setOnClickListener {
            selectedOption = "C"
            checkAnswer()
        }

        binding.alphabetOptD.setOnClickListener {
            selectedOption = "D"
            checkAnswer()
        }



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

    private fun checkAnswer() {
        if (selectedOption == correctOption) {
            CustomAlert.showCustomAlert(
                this,
                AlertType.CORRECT,
                "Correct Answer",
                "You did Great"
            ) {


            }
        } else {
            CustomAlert.showCustomAlert(
                this,
                AlertType.WRONG,
                "Wrong Answer",
                "Oops! Please try again"
            ) {


            }
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
            .setDuration(180)
            .withEndAction { binding.hintBubble.visibility = View.GONE }
            .start()

    }
}