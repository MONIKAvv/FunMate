package vv.monika.funMaatee

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import vv.monika.funMaatee.adapter.GameAdapter
import vv.monika.funMaatee.databinding.ActivityGameBinding
import vv.monika.funMaatee.model.GameItem
import vv.monika.funMaatee.model.ScoreViewModel

class GameActivity : AppCompatActivity() {
//    AppCompatActivity provides the activity ko perform backends and all

    private lateinit var binding: ActivityGameBinding
    private lateinit var scoreVM: ScoreViewModel
    private var dailyLimit = 0;

    private var gameStartTime: Long = 0L
    private var isGameRunning = false

    companion object {
        private const val GAME_TIMER_DURATION = 30000L // 30 seconds
    }
//     companion object -> this is used when there is something some common value or utility functions that will be same for all objects
//     companion object variables or functions used with the classname.varibale/function


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreVM = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ScoreViewModel::class.java)
//its like jb mera screen rotate hota h to data lose nhi hoga, if verticle se landscpae pr aaya then it still sotes the data throw viewModel

        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { currentScore ->
                binding.totalCoin.text = "$currentScore"
            }
        }
//        updating the score as soon as the activity loaded

        binding.backButton.setOnClickListener { finish() }

        val data = getDummyGames()
        setupRecyclerView(data)
    }
    private fun handleGameClick(item: GameItem) {
        Toast.makeText(this, "Clicked ${item.title}", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Please wait for 30 sec", Toast.LENGTH_SHORT).show()

dailyLimit++
        // Track when user starts playing
        gameStartTime = System.currentTimeMillis()
        isGameRunning = true

        openCustomTab(item.url)
//        dataclass se url leke opencustom tab ko de rha h

        GameNotifier.showNotification(
            context = this,
            type = AlertType.START,

            title = "Running",
            description = "Please wait for the timer to finish",
            onClaimClick = {
                scoreVM.addScore(+10)
                saveUserDataToServer()
                isGameRunning = false
            },
            countDownTimer = GAME_TIMER_DURATION.toInt()
        )
    }

    private fun saveUserDataToServer() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null){
            return
        }
        val json = JSONObject().apply {
            put("uid", currentUser.uid)
            put("name", currentUser.displayName ?: "")
            put("email", currentUser.email ?: "")
            put("device_id", android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID))
            put("coins", 10)
            put("subject", "gamefun")
            put("daily_limits", dailyLimit)
        }

        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            json.toString()
        )

        RetrofitBuilder.api.SaveUserData(body).enqueue(object :retrofit2.Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                try {

                    if (response.isSuccessful){
                        val responseString = response.body()?.string() ?: ""
                        Log.d("Tag", "Game data saved: ${responseString}")
                        if (responseString.isNotEmpty()){
                            val responseJson = JSONObject(responseString)
                            val success = responseJson.getBoolean("success")
                            if (success) {
                                val userData = responseJson.getJSONObject("user")
                                val totalCoins = userData.getInt("coins")
                                val gameLimit = userData.getInt("daily_limits")

                                Log.d("SERVER_SUCCESS", "Total Coins: $totalCoins, game Progress: $gameLimit")
                            } else {
                                val message = responseJson.getString("message")
                                Log.e("SERVER_ERROR", "Save failed: $message")
                            }
                        }
                    }else{
                        val errorString = response.errorBody()?.string() ?: "Unknown error"
                    }

                }catch (e: Exception){
                    Log.d("Tag", "Exception: ${e.message}")
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("Tag", "Network failure: ${t.message}")
            }
        })

    }

    private fun getDummyGames() = listOf(
        GameItem(1, "Stumble Guys", R.drawable.gameimage1, "https://www.stumbleguys.com/"),
        GameItem(2, "Minecraft", R.drawable.minecraft, "https://www.minecraft.net/"),
        GameItem(3, "Free Fire", R.drawable.gameimage1, "https://ff.garena.com/"),
        GameItem(4, "Among Us", R.drawable.minecraft, "https://innersloth.com/gameAmongUs.php"),
        GameItem(5, "Minecraft", R.drawable.minecraft, "https://www.minecraft.net/"),
        GameItem(6, "Free Fire", R.drawable.gameimage1, "https://ff.garena.com/"),
        GameItem(7, "Among Us", R.drawable.minecraft, "https://innersloth.com/gameAmongUs.php"),
        GameItem(8, "Stumble Guys", R.drawable.gameimage1, "https://www.stumbleguys.com/")
    )

    private fun setupRecyclerView(data: List<GameItem>) {
        val spanCount = 2
        val spacingPx = resources.getDimensionPixelSize(R.dimen.grid_spacing)

        binding.gameRecyclerView.apply {
            layoutManager = GridLayoutManager(this@GameActivity, spanCount)
            addItemDecoration(GridSpacingItemDecoration(spanCount, spacingPx, true))
            adapter = GameAdapter(data) { item -> handleGameClick(item) }
        }
    }


//resume is used when we want something to resume if we shifts to another place
    override fun onResume() {
        super.onResume()
        if (isGameRunning) {
            val elapsedTime = System.currentTimeMillis() - gameStartTime
//            elapsedTime checking ki kitna time beet gya hai
            if (elapsedTime < GAME_TIMER_DURATION) {
                // User returned too early -> mark as failed
                GameNotifier.failEarly()
//                if user comes before time then it shows error
                isGameRunning = false
            }
        }
    }

    private fun openCustomTab(url: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}

