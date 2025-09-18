package vv.monika.funMaatee

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import vv.monika.funMaatee.data.AlphabetFunQuestion
import vv.monika.funMaatee.databinding.ActivityAlphabetFunBinding
import vv.monika.funMaatee.model.ScoreViewModel
import kotlin.getValue

class AlphabetFunActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlphabetFunBinding
    //    fragment me activityViewModels hota h esme yesa hi h
    private val scoreVM: ScoreViewModel by viewModels()
    private var isLimitAlertShown = false
    private var practiceMode = false
    private val totalQuestions = 5
    private var currentIndex = 0
    private var hasAnswered = false
    private lateinit var currentQuestion: AlphabetFunQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAlphabetFunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val subject = "AlphabetFun"
        reFreshDailyCount(this, subject)

        setUpListeners()
        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { score ->
                binding.totalCoin.text = "$score"
            }
        }

        lifecycleScope.launchWhenStarted {

            val today = getTodayDate()
            val dateKey = getDateKey(subject)
            val countKey = getCountKey(subject)
            val prefs = applicationContext.dataStore.data.first()
            val lastDate = prefs[dateKey]?: ""
            val count = prefs[countKey]?: 0

            currentIndex = if (lastDate == today) count else 0
            practiceMode = currentIndex >= totalQuestions

            if (practiceMode){
                showDailyLimitReached()
            }
            loadNextQuestion()
            binding.currentQue.text = "${currentIndex} / $totalQuestions"
        }
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
        val wasUnderLimit = !practiceMode
        if (isCorrect && wasUnderLimit) {
            currentIndex++

            lifecycleScope.launchWhenStarted {
                incrementQuestionCount(this@AlphabetFunActivity, "AlphabetFun")
            }
        }
        val nowReachedOrOverLimit = currentIndex >= totalQuestions
        if (wasUnderLimit) {

            CustomAlert.showCustomAlert(
                context = this,
                type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
                title = if (isCorrect) "Correct! 🎉" else "Wrong Answer ❌",
                description = if (isCorrect) "Well done!" else "Try again!",
                onNextClick = {
                    Congrats.showCongratsAlert(
                        context = this,
                        onClaimClick = {
                            if (isCorrect) scoreVM.addScore(+1) // coins only under limit
                            binding.currentQue.text = "${currentIndex} / $totalQuestions"
                            hideHint()

                            // Agar abhi limit hit ho gayi to practice mode me shift karo
                            if (nowReachedOrOverLimit) {
                                practiceMode = true
                                showDailyLimitReachedOnce()
                            }

                            loadNextQuestion()
                            hasAnswered = false
                        },
                        10000
                    )
                },
                onCloseClick = {
                    hasAnswered = false
                    setOptionsEnabled(true)
                },
            )
        } else{
            CustomAlert.showCustomAlert(
                context = this,
                type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
                title = if (isCorrect) "Correct! 🎉" else "Wrong Answer ❌",
                description = if (isCorrect) "Well done!" else "Try again!",
                onNextClick = {
                    loadNextQuestion()
                    hasAnswered = false
                },
                onCloseClick = {
                    hasAnswered = false
                    setOptionsEnabled(true)
                }
            )
        }

    }

    private fun loadNextQuestion() {
        hideHint()

        currentQuestion = generateQuestion()
        renderQuestion(currentQuestion)

    }

    private fun renderQuestion(q: AlphabetFunQuestion) {
        hasAnswered = false
        binding.questionTextview.text = "${q.question} is similar to = ?"

        binding.optionA.text = q.options[0]
        binding.optionB.text = q.options[1]
        binding.optionC.text = q.options[2]
        binding.optionD.text = q.options[3]

        setOptionsEnabled(true)


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
    private var isHintVisible = false
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
        isHintVisible = false
    }
    private fun showDailyLimitReachedOnce(){
        if (isLimitAlertShown) return
        isLimitAlertShown = true
        showDailyLimitReached()
    }
    private fun showDailyLimitReached() {
        hideHint()
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CONGRATULATION,
            title = "Quiz Finished 🎉",
            description = "You reached to your daily limit \n Please visit next day!",
            onNextClick = { }
        )
    }
}
