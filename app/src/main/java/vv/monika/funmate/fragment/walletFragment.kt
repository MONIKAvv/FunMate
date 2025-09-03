package vv.monika.funmate.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.FragmentNavigatorExtras
import vv.monika.funmate.databinding.ActivityRadeemCodeBinding
import vv.monika.funmate.databinding.FragmentWalletBinding
import vv.monika.funmate.walletRadeemActivity

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)

        // ✅ Amazon Pay
        binding.amazonPay.setOnClickListener {
//            openApp("in.amazon.mShop.android.shopping", "Amazon Pay is not installed")
            startActivity(Intent(context, walletRadeemActivity::class.java))
        }

        // ✅ Google Pay
        binding.googlePay.setOnClickListener {
//            openApp("com.google.android.apps.nbu.paisa.user", "Google Pay is not installed")
//            val gpayIntent = Intent(Intent.ACTION_VIEW)
//            gpayIntent.data = Uri.parse("upi://pay?pa=merchant@upi&pn=Merchant&am=100&cu=INR")
//            gpayIntent.setPackage("com.google.android.apps.nbu.paisa.user") // Force Google Pay
//
//            try {
//                startActivity(gpayIntent)
//            } catch (e: Exception) {
//                Toast.makeText(requireContext(), "Google Pay not installed", Toast.LENGTH_SHORT).show()
//            }

            startActivity(Intent(context, walletRadeemActivity::class.java))


        }

        // ✅ Flipkart Pay (part of Flipkart app)
        binding.flipkartPay.setOnClickListener {
//            openApp("com.flipkart.android", "Flipkart app is not installed")
            startActivity(Intent(context, walletRadeemActivity::class.java))
        }

        // ✅ UPI Pay (Generic UPI Chooser)
        binding.upiPay.setOnClickListener {
//            val uri = android.net.Uri.parse(
//                "upi://pay?pa=merchant@upi&pn=Merchant&am=100&cu=INR"
//            )
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//
//            // Always show chooser
//            val chooser = Intent.createChooser(intent, "Pay with any UPI app")
//            startActivity(chooser)

       startActivity(Intent(context, walletRadeemActivity::class.java))

        }


        return binding.root
    }

    // ✅ Helper function to open apps safely
    private fun openApp(packageName: String, notInstalledMsg: String) {
        val launchIntent = requireActivity().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            Toast.makeText(requireContext(), notInstalledMsg, Toast.LENGTH_SHORT).show()
        }
    }
}
