package vv.monika.funmate

import android.app.AlertDialog
import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.airbnb.lottie.LottieAnimationView

object Congrats {

    fun showCongratsAlert(
        context: Context,
        onClaimClick: (() -> Unit)? = null,
        CountDownTimer: Int
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.congrats, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView).setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val lottieView = dialogView.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        val timerText = dialogView.findViewById<TextView>(R.id.timerText)
        val claimButton = dialogView.findViewById<Button>(R.id.claimButton)

        lottieView.playAnimation()

        // Initially hide claim button until timer finishes
        claimButton.visibility = android.view.View.GONE
        var isTimerFinished = false

        // Countdown 3 seconds
        object : CountDownTimer(CountDownTimer.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerText.text = "$secondsLeft"
            }

            override fun onFinish() {
                timerText.visibility = android.view.View.GONE
                claimButton.visibility = android.view.View.VISIBLE
            }
        }.start()

        // When claim clicked
        claimButton.setOnClickListener {
            alertDialog.dismiss()
            onClaimClick?.invoke()
        }


    }


}
