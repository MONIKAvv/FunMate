package vv.monika.funmate

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Row

object GameNotifier {
    private var currentDialog: AlertDialog? = null
    fun showNotification(
        context: Context,
        type: AlertType,
        title: String,
        description: String,
        onClaimClick: (() -> Unit)? = null,
        countDownTimer: Int
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.game_notifier, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView).setCancelable(false)

        val alertDialog = dialogBuilder.create()
        currentDialog = alertDialog
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        val timerText = dialogView.findViewById<TextView>(R.id.timer_text)
        val claimButton = dialogView.findViewById<Button>(R.id.claim_btn)
        val titleView = dialogView.findViewById<TextView>(R.id.notification_title)
        val icon = dialogView.findViewById<ImageView>(R.id.noticifacation_icon)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_notification)
        val message = dialogView.findViewById<TextView>(R.id.notification_description)

        titleView.text = title
        message.text = description


        claimButton.visibility = android.view.View.GONE
        var isTimerFinished = false

        object : CountDownTimer(countDownTimer.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerText.text = "$secondsLeft"
            }

            override fun onFinish() {
                isTimerFinished = true
                timerText.visibility = android.view.View.GONE
                claimButton.visibility = android.view.View.VISIBLE
            }
        }.start()
        claimButton.setOnClickListener {
            if (isTimerFinished) {
                message.text = "Now you can Claim"
                alertDialog.dismiss()
                onClaimClick?.invoke()
            } else {
                titleView.text = "Failed!"
                message.text = "You tried to claim before the timer finished."
                icon.setImageResource(R.drawable.wrong_icon)

                // Disable claim button after failed
                claimButton.visibility = android.view.View.GONE
                timerText.visibility = android.view.View.GONE
            }
        }

        when (type) {
            AlertType.WRONG -> icon.setImageResource(R.drawable.wrong_icon)
            AlertType.CORRECT -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.START -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.APPROVAL -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.CONGRATULATION -> icon.setImageResource(R.drawable.correct_icon)
        }

        closeBtn.setOnClickListener {
            if (titleView.text == "Failed!") {
                // Second click → dismiss dialog
                alertDialog.dismiss()
            } else {
                // First click → show failed message
                titleView.text = "Failed!"
                message.text = "You tried to claim before the timer finished."
                icon.setImageResource(R.drawable.wrong_icon)

                // Disable claim button after failed

                timerText.visibility = View.GONE
            }
        }

    }

    fun dismissAll() {
        currentDialog?.dismiss()
        currentDialog = null
    }
}