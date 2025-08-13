package vv.monika.funmate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vv.monika.funmate.databinding.ActivityCheckInBinding

class CheckInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkInStartBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        binding.backButton.setOnClickListener {
            finish()
        }

    }
}