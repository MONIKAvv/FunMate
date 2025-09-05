package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import vv.monika.funmate.data.MathsQuestion
import vv.monika.funmate.databinding.ActivityMatchFunBinding
import vv.monika.funmate.model.ScoreViewModel
import kotlin.random.Random

class MatchFunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchFunBinding
    private val scoreVM: ScoreViewModel by viewModels()

    private val dailyTotalQuestions = 1
    private var currentIndex = 0
    private var hasAnswered = false

    private lateinit var currentQuestion: MathsQuestion

    // --- Model types ---
    enum class Op(val symbol: String, val apply: (Int, Int) -> Int) {
        ADD("+", { a, b -> a + b }),
        SUB("-", { a, b -> a - b }),
        MUL("×", { a, b -> a * b }),
        MOD("%", { a, b -> a % b })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMatchFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { score ->
                binding.totalCoin.text = "$score"
            }
        }
//check if user can play today
//        lifecycleScope.launchWhenStarted {
//            canAttemptQuestion(this@MatchFunActivity, "Math",dailyTotalQuestions).collect { canPlay ->
//                if(canPlay){
//                    setupListeners()
//                    loadNextQuestion()
//                }else{
//                    showDailyLimitReached()
//                }
//            }
//        }


        setupListeners()
        loadNextQuestion()
    }

    // ---------------- UI / Listeners ----------------
    private fun setupListeners() {
        binding.backButton.setOnClickListener { finish() }
        binding.btnHint.setOnClickListener { toggleHint() }
        binding.skipBtn.setOnClickListener { loadNextQuestion() }

        binding.optionA.setOnClickListener { onOptionClicked(0) }
        binding.optionB.setOnClickListener { onOptionClicked(1) }
        binding.optionC.setOnClickListener { onOptionClicked(2) }
        binding.optionD.setOnClickListener { onOptionClicked(3) }

//        binding.nextButton.setOnClickListener { loadNextQuestion() }
    }

    private fun renderQuestion(q: MathsQuestion) {
        hasAnswered = false
        binding.questionTextview.text = "${q.a} ${q.op.symbol} ${q.b} = ?"


        // Assign options
//        binding.optionALabel.text = "A"
        binding.optionA.text = q.options[0].toString()
//        binding.optionBLabel.text = "B"
        binding.optionB.text = q.options[1].toString()
//        binding.optionCLabel.text = "C"
        binding.optionC.text = q.options[2].toString()
//        binding.optionDLabel.text = "D"
        binding.optionD.text = q.options[3].toString()


        // Reset visual states (if you have selected/background styles)
        setOptionsEnabled(true)
        clearOptionStyles()

        // Progress


    }

    private fun setOptionsEnabled(enabled: Boolean) {
        binding.optionA.isEnabled = enabled
        binding.optionB.isEnabled = enabled
        binding.optionC.isEnabled = enabled
        binding.optionD.isEnabled = enabled
    }

    private fun clearOptionStyles() {
        // TODO: Reset background/tint on your option views if you style them.
    }

    private fun markCorrectWrong(chosenIndex: Int) {
        // TODO: Optionally color the selected view red/green.
        // Example if they are Buttons:
        // val views = listOf(binding.optionA, binding.optionB, binding.optionC, binding.optionD)
        // views[currentQuestion.correctIndex].setBackgroundResource(R.drawable.bg_correct)
        // if (chosenIndex != currentQuestion.correctIndex) views[chosenIndex].setBackgroundResource(R.drawable.bg_wrong)
    }

    private fun onOptionClicked(index: Int) {
        if (hasAnswered) return
        hasAnswered = true
        setOptionsEnabled(false)

        val isCorrect = index == currentQuestion.correctIndex

        markCorrectWrong(index)
        if (isCorrect) {
            currentIndex++

            lifecycleScope.launchWhenStarted {
                incrementQuestionCount(this@MatchFunActivity, "Math")
            }
        }
        // If you have a custom dialog util, use it. Otherwise a simple Toast works.

        if (currentIndex > dailyTotalQuestions) {
            showDailyLimitReached()
        } else {
            CustomAlert.showCustomAlert(
                context = this,
                type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
                title = if (isCorrect) "Correct!" else "Wrong Answer",
                description = if (isCorrect) "Well done! 🎉" else "Try again! 💪",
                onNextClick = {
                    Congrats.showCongratsAlert(
                        context = this,
                        onClaimClick = {
                            scoreVM.addScore(+1)
                            binding.currentQue.text = "${currentIndex} / $dailyTotalQuestions"
                            hideHint()
                            loadNextQuestion()
                            // Back to MathActivity → load next question
                            hasAnswered = false

                        },
                        1000
                    )
                },
                onCloseClick = {
                    hasAnswered = false
                    setOptionsEnabled(true)
                }
            )
        }


        // Simple fallback: automatically go to next question after a short delay
//        binding.root.postDelayed({
//            loadNextQuestion()
//        }, 600)
    }

    private fun loadNextQuestion() {
        hideHint()

//        if (currentIndex >= dailyTotalQuestions) {
//            showDailyLimitReached()
//            return
//        }

        currentQuestion = generateQuestion()
        renderQuestion(currentQuestion)

    }

    private fun showDailyLimitReached() {
        hideHint()
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CONGRATULATION,
            title = "Quiz Finished 🎉",
            description = "You reached to your daily limit \n Please visit next day!",
            onNextClick = { finish() }
        )
        loadNextQuestion()
    }

    // ---------------- Question generation ----------------
    private fun generateQuestion(): MathsQuestion {
        val op = Op.values().random()
//        val op = Op.entries.toTypedArray().random()

        // Keep numbers in 1..100; ensure non-negative subtraction if you want (swap a/b)
        var a = Random.nextInt(1, 101)
        var b = Random.nextInt(1, 101)

        if (op == Op.SUB && a < b) {
            // Make subtraction non-negative (optional)
            val tmp = a; a = b; b = tmp
        }

        val answer = op.apply(a, b)
        val options = buildOptions(answer)
        val correctIdx = options.indexOf(answer)
        return MathsQuestion(a, b, op, answer, options, correctIdx)
    }

    private fun buildOptions(answer: Int): List<Int> {
        // Ensure 4 unique options including the correct answer
        val set = linkedSetOf(answer)
        var guard = 0
        while (set.size < 4 && guard < 200) {
            guard++
            // Generate plausible distractors near the answer plus an occasional random outlier
            val near = answer + listOf(-10, -5, -3, -2, -1, 1, 2, 3, 5, 10).random()
            val candidate = if (Random.nextInt(100) < 80) near else Random.nextInt(-200, 401)
            if (candidate != answer) set.add(candidate)
        }
        // Shuffle for random placement
        return set.shuffled()
    }

    // ---------------- Hint UI (your originals kept) ----------------
    private var isHintVisible = false

    private fun toggleHint() {
        if (isHintVisible) hideHint() else showHint()
        isHintVisible = !isHintVisible
    }

    private fun showHint() {
        binding.btnHint.setImageResource(R.drawable.close_hint)

        val correctAnswerValue = currentQuestion.answer

        binding.hintBubble.text =
            "Answer: ${currentQuestion.a} ${currentQuestion.op.symbol} ${currentQuestion.b} = ${currentQuestion.answer}"

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

}
