package vv.monika.funMaatee

import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Response
import vv.monika.funMaatee.data.ApiResponse
import vv.monika.funMaatee.databinding.ActivityPromoBinding
import vv.monika.funMaatee.model.ScoreViewModel

class PromoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPromoBinding
    private val scoreVM: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPromoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            scoreVM.score.collect { score ->
                binding.totalCoin.text = "$score"
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.applyPromocodeBtn.setOnClickListener {
            fetchPromocode()

        }
        binding.joinTelegram.setOnClickListener {
            Toast.makeText(this, "Join our telegram", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPromocode() {
        RetrofitBuilder.run {
            api.getPromocode().enqueue(object : retrofit2.Callback<ApiResponse>{

                override fun onResponse(
                    call: retrofit2.Call<ApiResponse?>,
                    response: Response<ApiResponse?>
                ) {
                    if(response.isSuccessful){
                        val promocodes = response.body()?.data
                       if (promocodes?.isNotEmpty() == true){
                           promocodes?.forEach {
                               Log.d("promocode","Code : ${promocodes} | Value: ${it.value}")
                           }
                       }else{
                           Toast.makeText(this@PromoActivity, "No Promocode", Toast.LENGTH_SHORT).show()
                       }
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ApiResponse?>,
                    t: Throwable
                ) {
                    Log.e("promocode", t.message.toString())
                }

            })
        }

    }
}