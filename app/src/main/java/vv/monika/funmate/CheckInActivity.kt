package vv.monika.funmate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import vv.monika.funmate.databinding.ActivityCheckInBinding
import vv.monika.funmate.model.ScoreViewModel

class CheckInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInBinding
    private val scoreVM : ScoreViewModel by viewModels()

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

        binding.checkInStartBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        binding.backButton.setOnClickListener {
            finish()
        }

    }
}