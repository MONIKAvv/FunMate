package vv.monika.funMaatee

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import vv.monika.funMaatee.databinding.ActivityCheckInBinding
import vv.monika.funMaatee.model.ScoreViewModel
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

                            saveCheckInDate(getTodayDate())

                        // ✅ Save only after claiming
                        }
                        scoreVM.addScore(score)
                        saveUserDataToServer(score)
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

    private fun saveUserDataToServer(score: kotlin.Int) {
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
                put("coins", score) // Send +1 coin increment
                put("subject", "dailycheckin") // THIS IS KEY - specify the subject
                put("daily_limits", totalSpin) // Current progress for Math
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
                            Log.d("SERVER_SUCCESS", "Math data saved: $responseString")

                            if (responseString.isNotEmpty()) {
                                val responseJson = JSONObject(responseString)
                                val success = responseJson.getBoolean("success")

                                if (success) {
                                    val userData = responseJson.getJSONObject("user")
                                    val totalCoins = userData.getInt("coins")
                                    val dailycheckinLimit = userData.getInt("daily_limits")

                                    Log.d("SERVER_SUCCESS", "Total Coins: $totalCoins, daily checkin Progress: $dailycheckinLimit")
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

}