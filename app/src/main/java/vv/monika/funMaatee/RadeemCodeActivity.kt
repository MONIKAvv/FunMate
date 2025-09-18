package vv.monika.funMaatee

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vv.monika.funMaatee.databinding.ActivityRadeemCodeBinding

class RadeemCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRadeemCodeBinding


//   fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = ActivityRadeemCodeBinding.inflate(inflater, container, false)
//
//        // ✅ Amazon Pay
//        binding.amazonPay.setOnClickListener {
//            openApp("in.amazon.mShop.android.shopping", "Amazon Pay is not installed")
//        }
//
//        // ✅ Google Pay
//        binding.googlePay.setOnClickListener {
//            openApp("com.google.android.apps.nbu.paisa.user", "Google Pay is not installed")
//        }
//
//        // ✅ Flipkart Pay (part of Flipkart app)
//        binding.flipkartPay.setOnClickListener {
//            openApp("com.flipkart.android", "Flipkart app is not installed")
//        }
//
//        // ✅ UPI Pay (Generic UPI Chooser)
//        binding.upiPay.setOnClickListener {
//            val uri = android.net.Uri.parse(
//                "upi://pay?pa=merchant@upi&pn=Merchant&am=10&cu=INR"
//            )
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            val chooser = Intent.createChooser(intent, "Pay with")
//            startActivity(chooser)
//        }
//
//        return binding.root
//    }

    // ✅ Helper function to open apps safely
    private fun openApp(packageName: String, notInstalledMsg: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            Toast.makeText(this, notInstalledMsg, Toast.LENGTH_SHORT).show()
        }
    }
}