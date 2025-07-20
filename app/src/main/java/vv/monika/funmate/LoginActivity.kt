package vv.monika.funmate

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vv.monika.funmate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.termsAndCondition.text = Html.fromHtml(
            "<font color='#888888'>I agree to the </font><b><font color='#FFFFFF'>Privacy Policy </font></b><font color='#888888'>and </font><b><font color='#FFFFFF'>Terms of Services</font></b>"

        )

    }
}