package vv.monika.funmate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Http2Reader
import vv.monika.funmate.databinding.ActivityCheckInBinding
import vv.monika.funmate.model.ScoreViewModel
import kotlin.random.Random

class CheckInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInBinding
    private val scoreVM: ScoreViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var boxes: List<ImageView>

    private var currentIndex = 0
    private var totalSteps = 0
    private var stepsDone = 0
    private var delay = 100L
    private var totalSpin = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { score ->
                binding.totalCoin.text = "$score"
            }
        }
        lifecycleScope.launchWhenStarted {
            val today = getTodayDate()  // same function you use in MatchFun
            val dateKey = getCheckInDateKey()

            val prefs = applicationContext.dataStore.data.first()
            val lastCheckIn = prefs[stringPreferencesKey(dateKey)] ?: ""

            if (lastCheckIn == today) {
                if (totalSpin >=3){
                    binding.checkInStartBtn.isEnabled = false
                    CustomAlert.showCustomAlert(this@CheckInActivity,
                        type = AlertType.APPROVAL,
                        title = "Completed",
                        description = "Your Daily CheckIn Completed, \n Please visit next day!",
                        onNextClick = {finish()}
                    )
                }

            } else {
                binding.checkInStartBtn.setOnClickListener {
                    lifecycleScope.launch {   // ✅ Coroutine body starts here
                        saveCheckInDate(today)
                        startSpin()
                    }
                }
            }
        }

        boxes = listOf(
            binding.ck1,
            binding.ck2,
            binding.ck3,
            binding.ck4,
            binding.ck6, binding.ck7, binding.ck8, binding.ck9
        )
        binding.backButton.setOnClickListener {
            finish()
        }


    }

    private suspend fun saveCheckInDate(today: String) {
        applicationContext.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(getCheckInDateKey())] = today
        }
    }

    private fun startSpin() {
//        Reset
        totalSpin++
        stepsDone = 0
        delay = 100L
        currentIndex = 0
//         decide random stop after 2-4 rounds
        val rounds = Random.nextInt(2, 5)
        val stopIndex = Random.nextInt(boxes.size)
        totalSteps = rounds * boxes.size + stopIndex

        spinStep()
    }

    private fun spinStep() {
//        clear all highlights
        boxes.forEach { it.setBackgroundResource(R.drawable.box_bg) }
//        hightlight current
        boxes[currentIndex].setBackgroundResource(R.drawable.box_bg_highlight)

        stepsDone++
        currentIndex = (currentIndex + 1) % boxes.size
//        slow down towards end
        if (totalSteps - stepsDone < 8) {
            delay += 50

        }

        if (stepsDone <= totalSteps) {
            handler.postDelayed({ spinStep() }, delay)
        } else {
            onSpinComplete(currentIndex)
        }

    }

    private fun onSpinComplete(finalIndex: Int) {
        var score = Random.nextInt(10, 50)
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CORRECT,
            title = "Whoa!!!",
            description = "You earned ${score}, \n please do claim it💪",
            onNextClick = {
                Congrats.showCongratsAlert(
                    context = this,
                    onClaimClick = {
                        scoreVM.addScore(score)
                    },
                    3000
                )
            },
            onCloseClick = {
            }
        )
//        add score
    }
}