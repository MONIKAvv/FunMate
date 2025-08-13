package vv.monika.funmate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vv.monika.funmate.databinding.ActivityRadeemCodeBinding

class RadeemCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadeemCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRadeemCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
       binding.backButton.setOnClickListener {
           finish()
       }

    }
}