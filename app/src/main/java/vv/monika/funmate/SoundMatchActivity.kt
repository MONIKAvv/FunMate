package vv.monika.funmate

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.databinding.ActivitySoundMatchBinding

class SoundMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoundMatchBinding
    private var isHintVisible = false

    val correctOption = "C"
    var selectedOption: String? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySoundMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.playSound.setOnClickListener {
//            playsound with animation
            mediaPlayer = MediaPlayer.create(this, R.raw.moo_sound)
            mediaPlayer?.start()

            binding.audioWave.visibility = View.VISIBLE
            binding.audioWave.playAnimation()

            mediaPlayer?.setOnCompletionListener {
                binding.audioWave.pauseAnimation()
                it.release()
            }
        }

        binding.soundOptA.setOnClickListener {
            selectedOption = "A"
            checkAnswer()
        }
        binding.soundOptB.setOnClickListener {
            selectedOption = "B"
            checkAnswer()
        }
        binding.soundOptC.setOnClickListener {
            selectedOption = "C"
            checkAnswer()
        }
        binding.soundOptD.setOnClickListener {
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
        if (selectedOption == correctOption) {
            CustomAlert.showCustomAlert(this, AlertType.CORRECT, "Corrext Answer", "Move next") {


            }
        } else {
            CustomAlert.showCustomAlert(this, AlertType.WRONG, "Wrong", "Try again") {


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