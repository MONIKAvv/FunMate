package vv.monika.funMaatee

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import vv.monika.funMaatee.data.LocalQuestionRepository
import vv.monika.funMaatee.data.QuestionsItem
import vv.monika.funMaatee.databinding.ActivitySoundMatchBinding
import vv.monika.funMaatee.model.ScoreViewModel
import vv.monika.funMaatee.ui.QuestionDeckViewModel

class SoundMatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoundMatchBinding
    private val scoreVM: ScoreViewModel by viewModels()
    private val vm: QuestionDeckViewModel by viewModels()
    private var isHintVisible = false
    private var hasAnswered = false
    private var currentQuestion: QuestionsItem? = null

    //    limits questions
    private var practiceMode = false
    private var dailyTotalQuestions = 5
    private var isLimitAlertShown = false
    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySoundMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subject = "soundfun"
        reFreshDailyCount(this, subject)

        val repo = LocalQuestionRepository(this, "sound_questions.json")
        vm.initIfNeeded(repo, Subjects = "soundfun")
        setUpListener()

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
            val lastDate = prefs[dateKey] ?: ""
            val count = prefs[countKey] ?: 0

            currentIndex = if (lastDate == today) count else 0
            practiceMode = currentIndex >= dailyTotalQuestions

            if (practiceMode) {
                showDailyLimitReachedOnce()
            }
            loadNextQuestion()
            binding.currentQue.text = "$currentIndex / $dailyTotalQuestions"
        }


    }

    private fun showDailyLimitReachedOnce() {
        if (isLimitAlertShown) return
        isLimitAlertShown = true
        showDailyLimitReached()
    }

    private fun showDailyLimitReached() {
        hideHint()
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CONGRATULATION,
            title = "Completed!",
            description = "You reached to your daily limit \n Please visit next day! \n or you can practice here",
            onNextClick = { finish() }
        )

    }

    private fun loadNextQuestion() {
        hideHint()

        val q = vm.next()
        hasAnswered = false
        currentQuestion = q as QuestionsItem?

        binding.questionTextview.text = q?.Question
        binding.optionA.text = q?.OptionA
        binding.optionB.text = q?.OptionB
        binding.optionC.text = q?.OptionC
        binding.optionD.text = q?.OptionD

        val (currentIndex, total) = vm.progress()
        binding.hintBubble.text = q?.Hint ?: ""
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
        if (hasAnswered) return
        hasAnswered = true
        setOptionsEnabled(false)
        val q = currentQuestion ?: return
        val correct = q.AnswerIndex
        val isCorrect = (index == correct)

        val wasUnderLimit = !practiceMode

        if (isCorrect && wasUnderLimit) {
            currentIndex++
            lifecycleScope.launchWhenStarted {
                incrementQuestionCount(this@SoundMatchActivity, "soundfun")
            }

        }
        val nowReachedOrOverLimit = currentIndex >= dailyTotalQuestions
        if (wasUnderLimit){
            CustomAlert.showCustomAlert(
                context = this,
                type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
                title = if (isCorrect) "Correct!" else "Wrong Answer",
                description = if (isCorrect) "Well done! 🎉" else "Try again! 💪",
                onNextClick = {
                    Congrats.showCongratsAlert(
                        context = this,
                        onClaimClick = {
                            if (isCorrect) {
                                scoreVM.addScore(+1)
                                saveUserDataToServer()
                            } // coins only under limit
                            binding.currentQue.text = "${currentIndex} / $dailyTotalQuestions"
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
                }
            )
        }else{
            CustomAlert.showCustomAlert(
                context = this,
                type = if (isCorrect) AlertType.CORRECT else AlertType.WRONG,
                title = if (isCorrect) "Correct!" else "Wrong Answer",
                description = if (isCorrect) "Well done! 🎉" else "Try again! 💪",
                onNextClick = {
                    loadNextQuestion() // keep practicing
                    hasAnswered = false
//                    showDailyLimitReached() this will again and again dailyLimitREached Popup
                },
                onCloseClick = {
                    hasAnswered = false
                    setOptionsEnabled(true)
                }
            )
        }

    }

    private fun saveUserDataToServer() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if(currentUser == null){
            Log.d("Tag", "User not authorized ")
            return
        }

        val json = JSONObject().apply {
            put("uid", currentUser.uid)
            put("name", currentUser.displayName ?: "Unknown")
            put("email", currentUser.email ?: "")
            put("device_id", android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID))
            put("coins", 1) // Send +1 coin increment
            put("subject", "soundfun") // THIS IS KEY - specify the subject
            put("daily_limits", currentIndex) // Current progress for Math
            // Remove idToken and dailyLimits - not needed
        }
        Log.d("Tag", "sending math data: $json")

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json.toString()
        )
        RetrofitBuilder.api.SaveUserData(body).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val responseString = response.body()?.string() ?: ""
                        Log.d("Tag", "Sound data saved: $responseString")

                        if (responseString.isNotEmpty()) {
                            val responseJson = JSONObject(responseString)
                            val success = responseJson.getBoolean("success")

                            if (success) {
                                val userData = responseJson.getJSONObject("user")
                                val totalCoins = userData.getInt("coins")
                                val soundLimit = userData.getInt("daily_limits")

                                Log.d("SERVER_SUCCESS", "Total Coins: $totalCoins, Math Progress: $soundLimit")
                            } else {
                                val message = responseJson.getString("message")
                                Log.e("SERVER_ERROR", "Save failed: $message")
                            }
                        }
                    } else {
                        val errorString = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("SERVER_ERROR", "HTTP Error ${response.code()}: $errorString")
                    }
                } catch (e: Exception) {
                    Log.e("SERVER_ERROR", "Exception: ${e.message}")
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.e("SERVER_ERROR", "Network failure: ${t.message}")
            }
        })

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
