package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.data.BigVsSmallQuestion
import vv.monika.funmate.databinding.ActivityBigvsSmallBinding
import kotlin.random.Random
import kotlin.math.max



enum class Comparison(val prompt: String) {
    GREATER("is greater than = ?"),
    SMALLER("is less than = ?")
}

class BigvsSmallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBigvsSmallBinding
    private var isHintVisible = false

    private val totalQuestions = 50
    private var currentIndex = 0
    private var score = 0
    private var hasAnswered = false
    private lateinit var currentQuestion: BigVsSmallQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)          // ✅ correct signature
        enableEdgeToEdge()
        binding = ActivityBigvsSmallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
        loadNextQuestion()
    }

    // ---------- Flow ----------
    private fun loadNextQuestion() {
        hideHint() // always reset hint

        if (currentIndex >= totalQuestions) {
            showFinalScore()
            return
        }

        currentQuestion = generateQuestion()
        renderQuestion(currentQuestion)
        currentIndex++
    }

    private fun renderQuestion(q: BigVsSmallQuestion) {
        hasAnswered = false

        binding.questionTextview.text = "${q.question} ${q.comparison.prompt}"

        binding.optionA.text = q.options[0].toString()
        binding.optionB.text = q.options[1].toString()
        binding.optionC.text = q.options[2].toString()
        binding.optionD.text = q.options[3].toString()

        setOptionsEnabled(true)
        binding.currentQue.text = "${currentIndex + 1}/$totalQuestions"
        binding.totalCoin.text = "$score"
    }

    // ---------- Question generation ----------
    private fun generateQuestion(): BigVsSmallQuestion {
        // base >= 2 so GREATER always has at least one valid smaller number
        val base = Random.nextInt(2, 2001) // 2..2000
        val comparison = if (Random.nextBoolean()) Comparison.GREATER else Comparison.SMALLER

        val correctAnswer = when (comparison) {
            // base > ?  => correct must be < base
            Comparison.GREATER -> {
                val low = max(1, base - 200)
                Random.nextInt(low, base) // [low, base-1]
            }
            // base < ?  => correct must be > base
            Comparison.SMALLER -> {
                val high = base + 201
                Random.nextInt(base + 1, high) // [base+1, base+200]
            }
        }

        val options = buildOptions(base, comparison, correctAnswer)
        val correctIdx = options.indexOf(correctAnswer)

        return BigVsSmallQuestion(base, comparison, options, correctIdx)
    }

    /**
     * Build 3 WRONG options that explicitly violate the relation,
     * plus the one correct option. This guarantees only one correct answer.
     *
     * GREATER  (base > ?)  -> wrong options must be >= base
     * SMALLER  (base < ?)  -> wrong options must be <= base
     */
    private fun buildOptions(base: Int, comparison: Comparison, correct: Int): List<Int> {
        val options = mutableSetOf<Int>()
        options.add(correct)

        val guardLimit = 500
        var guards = 0

        fun randNearAbove(from: Int, to: Int): Int {
            // generate plausible numbers near [from..to], with some wider spreads
            val nearOffsets = listOf(0, 1, 2, 3, 5, 10, 20, 50, 100, 150, 200)
            return if (Random.nextInt(100) < 80) {
                val baseVal = from + Random.nextInt(max(1, to - from + 1))
                baseVal + nearOffsets.random()
            } else {
                Random.nextInt(1, 3001)
            }
        }

        fun randNearBelow(from: Int, to: Int): Int {
            val nearOffsets = listOf(0, 1, 2, 3, 5, 10, 20, 50, 100, 150, 200)
            return if (Random.nextInt(100) < 80) {
                val baseVal = to - Random.nextInt(max(1, to - from + 1))
                (baseVal - nearOffsets.random()).coerceAtLeast(1)
            } else {
                Random.nextInt(1, 3001)
            }
        }

        while (options.size < 4 && guards++ < guardLimit) {
            val candidate = when (comparison) {
                Comparison.GREATER -> {
                    // WRONG must be >= base
                    val c = randNearAbove(base, base + 200)
                    if (c < base) continue
                    c
                }
                Comparison.SMALLER -> {
                    // WRONG must be <= base
                    val c = randNearBelow(max(1, base - 200), base)
                    if (c > base) continue
                    c
                }
            }

            if (candidate != correct && candidate != base) {
                options.add(candidate)
            }
        }

        return options.shuffled()
    }

    // ---------- Listeners ----------
    private fun setUpListeners() {
        binding.backButton.setOnClickListener { finish() }
        binding.btnHint.setOnClickListener { toggleHint() }

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
        if (isCorrect) score++

        CustomAlert.showCustomAlert(
            context = this,
            type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
            title = if (isCorrect) "Correct! 🎉" else "Wrong Answer ❌",
            description = if (isCorrect) "Well done!" else "Try again!",
            onNextClick = { loadNextQuestion() }
        )
    }

    private fun setOptionsEnabled(enabled: Boolean) {
        binding.optionA.isEnabled = enabled
        binding.optionB.isEnabled = enabled
        binding.optionC.isEnabled = enabled
        binding.optionD.isEnabled = enabled
    }

    // ---------- Hint ----------
    private fun toggleHint() {
        if (isHintVisible) hideHint() else showHint()
        isHintVisible = !isHintVisible
    }

    private fun showHint() {
        binding.btnHint.setImageResource(R.drawable.close_hint)
        val correct = currentQuestion.options[currentQuestion.correctIndex]
        binding.hintBubble.text =
            "Answer: ${currentQuestion.question} ${currentQuestion.comparison.prompt} $correct"

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

    // ---------- Finish ----------
    private fun showFinalScore() {
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CORRECT,
            title = "Quiz Finished 🎉",
            description = "Your final score: $score / $totalQuestions",
            onNextClick = { finish() }
        )
    }
}
