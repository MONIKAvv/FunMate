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
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import vv.monika.funmate.databinding.ActivityMatchFunBinding

class MatchFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchFunBinding
    private var isHintVisible = false
    private var currentQuestion: QuestionsItem? = null
    private var questionList: List<QuestionsItem>? = null
    private var currentQuestionIndex = 0

    var selectedOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "onCreate started")
        enableEdgeToEdge()
        binding = ActivityMatchFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        fetchQuestion()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.btnHint.setOnClickListener {
            toggleHint()
        }
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

                    if (result.isSuccessful) {
                        Log.d("result", "Success: ${result.body()}")
                        val questions = result.body()
                        Log.d("API", "Got ${questions?.size} questions")

//                        update ui
                        withContext(Dispatchers.Main) {
                            if (!questions.isNullOrEmpty()) {
                                questionList = questions
                                currentQuestionIndex = 0
                                displayCurrentQuestion()
                            } else {
                                showError("No Question Found")
                            }

                        }

                    } else {
                        withContext(Dispatchers.Main) {
                            showError("Failed to fetch question: ${result.message()}")
                        }
                        Log.e("API_ERROR", "Error: ${result.code()} - ${result.message()}")
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    showError("Request timed out")
                }
            } catch (e: Exception) {
                Log.e("API_Error", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                }
            }

            Log.d("result", "hey this - coroutine ended")
        }
    }

    private fun displayCurrentQuestion() {
        val questions = questionList ?: return
        if (currentQuestionIndex >= questions.size) return

        currentQuestion = questions[currentQuestionIndex]
        val question = currentQuestion ?: return

        // Update UI with question data
        binding.apply {
            // Set question text (assuming you have a TextView for question)
             questionTextview.text = question.Question

            // Set options
            mathOptA.text = question.OptionA ?: "Option A"
            mathOptB.text = question.OptionB ?: "Option B"
            mathOptC.text = question.OptionC ?: "Option C"
            mathOptD.text = question.OptionD ?: "Option D"

            // Reset selection
            selectedOption = null
            resetOptionColors()
        }

        Log.d("QUESTION", "Displaying: ${question.Question}")
        Log.d("QUESTION", "Answer: ${question.AnswerIndex}")
    }



    private fun resetOptionColors() {
        // Reset all option buttons to default state
        binding.apply {
//            mathOptA.setBackgroundResource(R.drawable.default_option_bg) // Your default background
//            mathOptB.setBackgroundResource(R.drawable.default_option_bg)
//            mathOptC.setBackgroundResource(R.drawable.default_option_bg)
//            mathOptD.setBackgroundResource(R.drawable.default_option_bg)
        }
    }

    private fun checkAnswer() {
        val correctAnswer = currentQuestion?.AnswerIndex


        if (selectedOption == correctAnswer) {
            Log.d("ANSWER", "Correct! Selected: $selectedOption, Answer: $correctAnswer")
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.CORRECT,
                title = "Correct!",
                description = "Well done! 🎉",
                onNextClick = {
                    loadNextQuestion()  // This will be called when Next button is clicked
                }
            )
        } else {
            Log.d("ANSWER", "Wrong! Selected: $selectedOption, Answer: $correctAnswer")
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.WRONG,
                title = "Wrong Answer",
                description = "Try again! 💪 , Its better if you solve this, if you want to move next, Click Next",
                onNextClick = {
                    // Reset for retry or load next question
                    selectedOption = null
                    resetOptionColors()
                    loadNextQuestion()
                    // Or you can call loadNextQuestion() here too if you want
                }
            )
        }
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++
        val questions = questionList ?: return

        if (currentQuestionIndex < questions.size) {
            displayCurrentQuestion()
        } else {
            // All questions completed
            CustomAlert.showCustomAlert(
                this,
                AlertType.CORRECT,
                "Completed!",
                "You've completed all questions! 🏆",
                onNextClick = {
                    finish() // Go back or restart
                }
            )
        }
    }

    private fun showError(message: String) {
        Log.e("ERROR", message)
        // Show error to user (Toast or Dialog)
        CustomAlert.showCustomAlert(this, AlertType.WRONG, "Error", message, onNextClick = {
            finish() // Go back on error
        })
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