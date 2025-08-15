package vv.monika.funmate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import vv.monika.funmate.databinding.ActivityMatchFunBinding

class MatchFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchFunBinding
    private var isHintVisible = false

    val correctOption = "A"
    var selectedOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "onCreate started")

        try {
            enableEdgeToEdge()
            Log.d("DEBUG", "Edge-to-edge enabled")

            binding = ActivityMatchFunBinding.inflate(layoutInflater)
            Log.d("DEBUG", "Binding inflated")

            setContentView(binding.root)
            Log.d("DEBUG", "Content view set")

            // Test each view one by one
            binding.backButton?.let {
                it.setOnClickListener { finish() }
                Log.d("DEBUG", "Back button found and set")
            } ?: Log.e("DEBUG", "backButton is NULL!")

            Log.d("DEBUG", "About to call fetchQuestion")
            fetchQuestion()
            Log.d("DEBUG", "fetchQuestion called")

            // Comment out all the other view setups for now to isolate the issue
            /*
            binding.mathOptA.setOnClickListener {
                selectedOption = "A"
                checkAnswer()
            }
            */

        } catch (e: Exception) {
            Log.e("CRASH", "onCreate crashed: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun fetchQuestion() {
        val questionAPI = RetrofitBuilder.getInstance().create(MyQuestionInterface::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("result", "hey - coroutine started")
            Log.d("API_DEBUG", "About to make API call...")

            try {
                Log.d("API_DEBUG", "Making request to: ${RetrofitBuilder.API}questions/Maths")

                // Add a timeout using withTimeout
                withTimeout(20000) { // 20 second timeout
                    val result = questionAPI.getQuestion()
                    Log.d("API_DEBUG", "Got response, processing...")

                    Log.d("API_RESPONSE", "Response code: ${result.code()}")
                    Log.d("API_RESPONSE", "Response body: ${result.body().toString()}")

                    if (result.isSuccessful) {
                        Log.d("result", "Success: ${result.body()}")
                    } else {
                        Log.e("API_ERROR", "Error: ${result.code()} - ${result.message()}")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.e("Api_Error", "Request timed out after 20 seconds")
            } catch (e: Exception) {
                Log.e("Api_Error", "Exception: ${e.message}")
                e.printStackTrace()
            }

            Log.d("result", "hey this - coroutine ended")
        }
    }

    private fun checkAnswer() {
        if (selectedOption == correctOption) {
            CustomAlert.showCustomAlert(this, AlertType.CORRECT, "Correct ", "oky well done")
        } else {
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