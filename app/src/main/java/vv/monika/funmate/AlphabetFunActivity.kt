package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.data.AlphabetFunQuestion
import vv.monika.funmate.databinding.ActivityAlphabetFunBinding

class AlphabetFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlphabetFunBinding
    private var isHintVisible = false

    private val totalQuestions = 100
    private var currentIndex = 0
    private var score = 0
    private var hasAnswered = false
    private lateinit var currentQuestion: AlphabetFunQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlphabetFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        loadNextQuestion() // load the first question
    }

    private fun setUpListeners() {
        binding.backButton.setOnClickListener { finish() }
        binding.btnHint.setOnClickListener { toggleHint() }
        binding.skipBtn.setOnClickListener { loadNextQuestion() }

        binding.optionA.setOnClickListener { onOptionClicked(0) }
        binding.optionB.setOnClickListener { onOptionClicked(1) }
        binding.optionC.setOnClickListener { onOptionClicked(2) }
        binding.optionD.setOnClickListener { onOptionClicked(3) }
    }

    private fun onOptionClicked(index: Int) {
        if (hasAnswered) return
        hasAnswered = true
        setOptionsEnabled(false)

        val isCorrect = index == currentQuestion.correctIndex
        if (isCorrect) {
            score++
        }

        CustomAlert.showCustomAlert(
            context = this,
            type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
            title = if (isCorrect) "Correct! 🎉" else "Wrong Answer ❌",
            description = if (isCorrect) "Well done!" else "Try again!",
            onNextClick = {  Congrats.showCongratsAlert(
                context = this,
                onClaimClick = {
                    // Back to MathActivity → load next question
                    hasAnswered = false
                    setOptionsEnabled(true)
                    loadNextQuestion()
                }
            ) },
            onCloseClick = {
                hasAnswered = false
                setOptionsEnabled(true)
            }
        )
    }

    private fun loadNextQuestion() {
        // Always hide hint for new question
        hideHint()

        if (currentIndex >= totalQuestions) {
            showFinalScore()
            return
        }

        currentQuestion = generateQuestion()
        renderQuestion(currentQuestion)
        currentIndex++
    }

    private fun renderQuestion(q: AlphabetFunQuestion) {
        hasAnswered = false
        binding.questionTextview.text = "${q.question} is similar to = ?"

        binding.optionA.text = q.options[0]
        binding.optionB.text = q.options[1]
        binding.optionC.text = q.options[2]
        binding.optionD.text = q.options[3]

        setOptionsEnabled(true)
        binding.currentQue.text = "${currentIndex}/$totalQuestions"
        binding.totalCoin.text = "$score"
    }

    private fun generateQuestion(): AlphabetFunQuestion {
        val length = (1..3).random()
        val letters = ('A'..'Z') + ('a'..'z')

        // Random string
        val randomString = (1..length).map { letters.random() }.joinToString("")

        // Flip case for correct answer
        val correctAnswer = randomString.map {
            if (it.isUpperCase()) it.lowercaseChar() else it.uppercaseChar()
        }.joinToString("")

        // Build options
        val options = buildOptions(correctAnswer)
        val correctIdx = options.indexOf(correctAnswer)

        return AlphabetFunQuestion(randomString, options, correctIdx)
    }

    private fun buildOptions(correctAnswer: String): List<String> {
        val letters = ('A'..'Z') + ('a'..'z')
        val options = mutableSetOf(correctAnswer)

        // Add 3 wrong answers
        while (options.size < 4) {
            val randomWrong = (1..correctAnswer.length)
                .map { letters.random() }
                .joinToString("")
            options.add(randomWrong)
        }

        return options.shuffled()
    }

    private fun setOptionsEnabled(enabled: Boolean) {
        binding.optionA.isEnabled = enabled
        binding.optionB.isEnabled = enabled
        binding.optionC.isEnabled = enabled
        binding.optionD.isEnabled = enabled
    }

    private fun toggleHint() {
        if (isHintVisible) hideHint() else showHint()
        isHintVisible = !isHintVisible
    }

    private fun showHint() {
        binding.btnHint.setImageResource(R.drawable.close_hint)
        val correctAnswer = currentQuestion.options[currentQuestion.correctIndex]
        binding.hintBubble.text = "Answer: ${currentQuestion.question} → $correctAnswer"

        binding.hintBubble.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = 20f
            animate().alpha(1f).translationY(0f).setDuration(120).start()
        }
    }

    private fun hideHint() {
        binding.btnHint.setImageResource(R.drawable.hint_icon)
        binding.hintBubble.animate()
            .alpha(0f)
            .translationY(20f)
            .setDuration(120)
            .withEndAction { binding.hintBubble.visibility = View.GONE }
            .start()

    }

    private fun showFinalScore() {
        binding.questionTextview.text = "Finished!"
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CONGRATULATION,
            title = "Quiz Finished 🎉",
            description = "Your final score: $score / $totalQuestions",
            onNextClick = { finish() }
        )
    }
}
