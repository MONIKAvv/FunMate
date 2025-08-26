package vv.monika.funmate

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funmate.data.LocalQuestionRepository
import vv.monika.funmate.databinding.ActivitySoundMatchBinding
import vv.monika.funmate.ui.QuestionDeckViewModel

class SoundMatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoundMatchBinding
    private val vm: QuestionDeckViewModel by viewModels()
    private var isHintVisible = false
    private var score = 0
    private var hasAnswered = false
    private var currentQuestion: QuestionsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySoundMatchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val repo = LocalQuestionRepository(this, "sound_questions.json")
        vm.initIfNeeded(repo, Subjects = "Sound")


        setUpListener()
        loadNextQuestion()

    }

    private fun loadNextQuestion() {
        hideHint()

        val q = vm.next()
        if (q == null) {
            binding.questionTextview.text = "Finished!"
            CustomAlert.showCustomAlert(
                context = this,
                type = AlertType.CONGRATULATION,
                title = "Completed!",
                description = "Your score: $score / ${vm.progress().second}",
                onNextClick = { finish() }
            )
            return
        }
        hasAnswered = false
        currentQuestion = q as QuestionsItem?

        binding.questionTextview.text = q.Question
        binding.optionA.text = q.OptionA
        binding.optionB.text = q.OptionB
        binding.optionC.text = q.OptionC
        binding.optionD.text = q.OptionD

        val (currentIndex, total) = vm.progress()
        binding.currentQue.text = currentIndex.toString()
        binding.totalQue.text = total.toString()
        binding.totalCoin.text = score.toString()
        binding.hintBubble.text = q.Hint ?: ""
        setOptionsEnabled(true)

    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener { finish() }
        binding.btnHint.setOnClickListener { toggleHint() }
        binding.skipBtn.setOnClickListener { loadNextQuestion() }

        binding.optionA.setOnClickListener { selectOption("A") }
        binding.optionB.setOnClickListener { selectOption("B") }
        binding.optionC.setOnClickListener { selectOption("C") }
        binding.optionD.setOnClickListener { selectOption("D") }

    }
    private fun setOptionsEnabled(enabled: Boolean) {
        binding.optionA.isEnabled = enabled
        binding.optionB.isEnabled = enabled
        binding.optionC.isEnabled = enabled
        binding.optionD.isEnabled = enabled
    }

    private fun selectOption(index: String) {
        val q = currentQuestion ?: return
        val correct = q.AnswerIndex
        val isCorrect = (index == correct)
        if (isCorrect) {
            score++
        }
        CustomAlert.showCustomAlert(
            context = this,
            type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
            title = if (isCorrect) "Correct! 🎉" else "Wrong Answer ❌",
            description = if (isCorrect) "Well done!" else "Correct was: $correct",
            onNextClick = {  Congrats.showCongratsAlert(
                context = this,
                onClaimClick = {
                    // Back to MathActivity → load next question
                    hasAnswered = false
                    setOptionsEnabled(true)
                    loadNextQuestion()
                },10000
            ) },
            onCloseClick = {
//                apply has answered things
            }
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
