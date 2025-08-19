package vv.monika.funmate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import vv.monika.funmate.databinding.ActivityBigvsSmallBinding
import vv.monika.funmate.databinding.ActivityMatchFunBinding

// Base class for all subject activities
abstract class BaseQuestionActivity : AppCompatActivity() {
    protected abstract fun getViewBinding(): ViewBinding

    protected lateinit var binding: ViewBinding
    protected var isHintVisible = false

    // Question data
    protected var currentQuestion: QuestionsItem? = null
    protected var questionsList: List<QuestionsItem>? = null
    protected var currentQuestionIndex = 0
    protected var selectedOption: String? = null

    // Abstract method - har subject activity me define it
    protected abstract fun getSubjectName(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = getViewBinding()
        setContentView(binding.root)

        setupClickListeners()
        fetchQuestions()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener { finish() }
        binding.btnHint.setOnClickListener { toggleHint() }

        // Remove checkAnswer() from click listeners
        binding.optionB.setOnClickListener { selectOption("A") }
        binding.optionB.setOnClickListener { selectOption("B") }
        binding.optionC.setOnClickListener { selectOption("C") }
        binding.optionD.setOnClickListener { selectOption("D") }
    }

    private fun selectOption(option: String) {
        selectedOption = option
        Log.d("SELECTION", "Selected option: $option")

        // Reset all options first
        resetOptionColors()

        // Highlight selected option
        when(option) {
//            "A" -> binding.mathOptA.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_option))
//            "B" -> binding.mathOptB.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_option))
//            "C" -> binding.mathOptC.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_option))
//            "D" -> binding.mathOptD.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_option))
        }

        // Check answer after a small delay to show selection
        binding.root.postDelayed({
            checkAnswer()
        }, 300)
    }

    protected fun fetchQuestions() {
        val questionAPI = RetrofitBuilder.getInstance().create(MyQuestionInterface::class.java)
        val subject = getSubjectName()

        showLoading(true)

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("API", "Fetching $subject questions...")

            try {
                withTimeout(20000) {
                    // Using generic method with subject parameter
                    val result = questionAPI.getQuestionsBySubject(subject)

                    if (result.isSuccessful) {
                        val questions = result.body()
                        Log.d("API", "Got ${questions?.size} $subject questions")

                        withContext(Dispatchers.Main) {
                            if (!questions.isNullOrEmpty()) {
                                questionsList = questions
                                currentQuestionIndex = 0
                                displayCurrentQuestion()
                            } else {
                                showError("No $subject questions found")
                            }
                            showLoading(false)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showError("Failed to fetch $subject questions: ${result.message()}")
                            showLoading(false)
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    showError("Request timed out")
                    showLoading(false)
                }
            } catch (e: Exception) {
                Log.e("API_Error", "Exception: ${e.message}")
                withContext(Dispatchers.Main) {
                    showError("Error: ${e.message}")
                    showLoading(false)
                }
            }
        }
    }

    private fun displayCurrentQuestion() {
        val questions = questionsList ?: return
        if (currentQuestionIndex >= questions.size) return

        currentQuestion = questions[currentQuestionIndex]
        val question = currentQuestion ?: return

        binding.apply {
            // Set options
            questionTextview.text = question.Question
            optionA.text = question.OptionA ?: "Option A"
            optionB.text = question.OptionB ?: "Option B"
            optionC.text = question.OptionC ?: "Option C"
            optionD.text = question.OptionD ?: "Option D"
            hintBubble.text = question.Hint

            selectedOption = null
            resetOptionColors()
        }

        Log.d("QUESTION", "Displaying ${getSubjectName()}: ${question.Question}")
        Log.d("QUESTION", "Correct Answer: ${question.AnswerIndex}")
    }

    private fun resetOptionColors() {
        binding.apply {
            // Use default background color
//            mathOptA.setBackgroundColor(ContextCompat.getColor(this@BaseQuestionActivity, R.color.default_option))
//            mathOptB.setBackgroundColor(ContextCompat.getColor(this@BaseQuestionActivity, R.color.default_option))
//            mathOptC.setBackgroundColor(ContextCompat.getColor(this@BaseQuestionActivity, R.color.default_option))
//            mathOptD.setBackgroundColor(ContextCompat.getColor(this@BaseQuestionActivity, R.color.default_option))
        }
    }

    private fun checkAnswer() {
        val correctAnswer = currentQuestion?.AnswerIndex
        val selected = selectedOption

        Log.d("ANSWER_CHECK", "Selected: $selected, Correct: $correctAnswer")

        if (selected == null) {
            Log.w("ANSWER_CHECK", "No option selected")
            return
        }

        if (selected == correctAnswer) {
            Log.d("ANSWER_CHECK", "Correct answer!")
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.CORRECT,
                title = "Correct!",
                description = "Well done! 🎉",
                onNextClick = { loadNextQuestion() }
            )
        } else {
            Log.d("ANSWER_CHECK", "Wrong answer!")
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.WRONG,
                title = "Wrong Answer",
                description = "Try again! 💪",
                onNextClick = {
                    selectedOption = null
                    loadNextQuestion()
                    resetOptionColors()
                }
            )
        }
    }

    protected fun loadNextQuestion() {
        currentQuestionIndex++
        val questions = questionsList ?: return

        if (currentQuestionIndex < questions.size) {
            binding.apply {
                currentQue.text = currentQuestionIndex.toString()
                totalQue.text = questions.size.toString()
            }
            displayCurrentQuestion()
        } else {
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.CORRECT,
                title = "Completed!",
                description = "You've completed all ${getSubjectName()} questions! 🏆",
                onNextClick = { finish() }
            )
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            optionA.isEnabled = !show
            optionB.isEnabled = !show
            optionC.isEnabled = !show
            optionD.isEnabled = !show
        }
    }

    private fun showError(message: String) {
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.WRONG,
            title = "Error",
            description = message,
            onNextClick = { finish() }
        )
    }

    private fun toggleHint() {
        if (isHintVisible) hideHint() else showHint()
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