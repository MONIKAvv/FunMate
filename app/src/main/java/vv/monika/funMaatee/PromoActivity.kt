package vv.monika.funMaatee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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


    }
}