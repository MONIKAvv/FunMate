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
import java.util.Calendar
import kotlin.random.Random

class CheckInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInBinding
    private val scoreVM: ScoreViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var boxes: List<ImageView>
    private var  calendar = Calendar.getInstance()
   private var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    private lateinit var dayIcon: List<ImageView>

    private var todayIndex = when (dayOfWeek) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> 0
    }

    private var currentIndex = 0
    private var totalSteps = 0
    private var stepsDone = 0
    private var delay = 100L
    private var totalSpin = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

         dayIcon = listOf(
            binding.dIcon0,
            binding.dIcon1,
            binding.dIcon2,
            binding.dIcon3,
            binding.dIcon4,
            binding.dIcon5,
            binding.dIcon6,
        )

        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { score ->
                binding.totalCoin.text = "$score"
            }
        }
        highlightToday()
//        highlightAndShowCheckMarks()

        lifecycleScope.launchWhenStarted {
            val today = getTodayDate()  // same function you use in MatchFun
            val dateKey = getCheckInDateKey()

            val prefs = applicationContext.dataStore.data.first()
            val lastCheckIn = prefs[stringPreferencesKey(dateKey)] ?: ""

            if (lastCheckIn == today) {
                dayIcon[todayIndex].setImageResource(R.drawable.double_check)
                binding.checkInStartBtn.isEnabled = false
                CustomAlert.showCustomAlert(
                    this@CheckInActivity,
                    type = AlertType.APPROVAL,
                    title = "Completed",
                    description = "Your Daily Check-In Completed, \n Please visit next day!",
                    onNextClick = { finish() }
                )
            }else {
                binding.checkInStartBtn.setOnClickListener {
                    binding.checkInStartBtn.isEnabled = false // 🚀 Immediately disable button
                    startSpin()
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
//
//    private fun highlightAndShowCheckMarks() {
//        lifecycleScope.launch {
//            // Step 1: Get today's day name
//            val today = getTodayDate()
//            val calendar = Calendar.getInstance()
//            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1=Sunday
//
//            // Step 2: Prepare lists
//            val dayImages = listOf(
//                binding.imageView38, // Monday
//                binding.imageView44, // Tuesday
//                binding.imageView43, // Wednesday
//                binding.imageView42, // Thursday
//                binding.imageView41, // Friday
//                binding.imageView40, // Saturday
//                binding.imageView39  // Sunday
//            )
//
//            val iconList = listOf(
//                binding.monday, binding.tuesday, binding.wednesday,
//                binding.thrusday, binding.friday, binding.saturday, binding.sunday
//            )
//
//            // Reset all
//            dayImages.forEach { it.setImageResource(R.drawable.inactive__day) }
//
//            // Step 3: Highlight today's day
//            val todayIndex = when (dayOfWeek) {
//                Calendar.MONDAY -> 0
//                Calendar.TUESDAY -> 1
//                Calendar.WEDNESDAY -> 2
//                Calendar.THURSDAY -> 3
//                Calendar.FRIDAY -> 4
//                Calendar.SATURDAY -> 5
//                Calendar.SUNDAY -> 6
//                else -> 0
//            }
//            dayImages[todayIndex].setImageResource(R.drawable.active__day)
//
//            // Step 4: Get last saved check-in date
//            val prefs = applicationContext.dataStore.data.first()
//            val lastCheckIn = prefs[stringPreferencesKey(getCheckInDateKey())] ?: ""
//
//            // Step 5: If user already checked in today → show double_check
//            if (lastCheckIn == today) {
//                // Add double-check icon
//                val checkIcon = ImageView(this@CheckInActivity)
//                checkIcon.setImageResource(R.drawable.double_check)
//                // You can also add animations here if you want
//            }
//        }
//    }


    private fun highlightToday() {
        // Step 1: Get today's day name
        // 1=Sunday, 2=Monday, etc.

        // Step 2: Reset all days to inactive
        val days = listOf(
            binding.imageView38, // Monday
            binding.imageView44, // Tuesday
            binding.imageView43, // Wednesday
            binding.imageView42, // Thursday
            binding.imageView41, // Friday
            binding.imageView40, // Saturday
            binding.imageView39  // Sunday
        )

        days.forEach { it.setImageResource(R.drawable.inactive__day) }
        dayIcon.forEach { it.setImageResource(R.drawable.timer) }

        // Step 3: Find correct index for today


        // Step 4: Highlight today's day
        days[todayIndex].setImageResource(R.drawable.active__day)

    }

    private suspend fun saveCheckInDate(today: String) {
        applicationContext.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(getCheckInDateKey())] = today
        }
    }

    private fun startSpin() {
//        Reset

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
        lifecycleScope.launch {
            saveCheckInDate(getTodayDate())
//            savees todays date
        }
        CustomAlert.showCustomAlert(
            context = this,
            type = AlertType.CORRECT,
            title = "Whoa!!!",
            description = "You earned ${score}, \n please do claim it💪",
            onNextClick = {
                Congrats.showCongratsAlert(
                    context = this,
                    onClaimClick = {
                        lifecycleScope.launch {
                            saveCheckInDate(getTodayDate()) // ✅ Save only after claiming
                        }
                        scoreVM.addScore(score)
                        dayIcon[todayIndex].setImageResource(R.drawable.double_check)
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