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
    private var titleView: TextView? = null
    private var messageView: TextView? = null
    private var iconView: ImageView? = null
    private var claimButton: Button? = null
    private var timerText: TextView? = null

    fun showNotification(
        context: Context,
        type: AlertType,
        title: String,
        description: String,
        onClaimClick: (() -> Unit)? = null,
        countDownTimer: Int
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.game_notifier, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create()
        currentDialog = dialog
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        titleView = dialogView.findViewById(R.id.notification_title)
        messageView = dialogView.findViewById(R.id.notification_description)
        iconView = dialogView.findViewById(R.id.noticifacation_icon)
        claimButton = dialogView.findViewById(R.id.claim_btn)
        timerText = dialogView.findViewById(R.id.timer_text)

        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_notification)

        titleView?.text = title
        messageView?.text = description
        claimButton?.visibility = View.GONE

        var isTimerFinished = false

        startTimer(countDownTimer, timerText!!) {
            isTimerFinished = true
            titleView?.text = "You Ready to Go!"
            messageView?.text = "Now you can Claim"
            timerText?.visibility = View.GONE
            claimButton?.visibility = View.VISIBLE
        }

        claimButton?.setOnClickListener {
            if (isTimerFinished) {
                dialog.dismiss()
                onClaimClick?.invoke()
            } else {
                showFailedState()
            }
        }

        closeBtn.setOnClickListener {
            if (titleView?.text == "Failed!") dialog.dismiss() else showFailedState()
        }

        setIcon(type, iconView!!)
    }

    private fun startTimer(duration: Int, timerText: TextView, onFinish: () -> Unit) {
        object : CountDownTimer(duration.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "${millisUntilFinished / 1000}"
            }
            override fun onFinish() = onFinish()
        }.start()
    }

    private fun showFailedState() {
        titleView?.text = "Failed!"
        messageView?.text = "You missed it, Try again!"
        iconView?.setImageResource(R.drawable.wrong_icon)
        claimButton?.visibility = View.GONE
        timerText?.visibility = View.GONE
    }

    fun failEarly() {
        if (currentDialog?.isShowing == true) {
            showFailedState()
        }
    }

    private fun setIcon(type: AlertType, icon: ImageView) {
        val iconMap = mapOf(
            AlertType.WRONG to R.drawable.wrong_icon,
            AlertType.CORRECT to R.drawable.correct_icon,
            AlertType.START to R.drawable.correct_icon,
            AlertType.APPROVAL to R.drawable.correct_icon,
            AlertType.CONGRATULATION to R.drawable.correct_icon
        )
        icon.setImageResource(iconMap[type] ?: R.drawable.correct_icon)
    }

    fun dismissAll() {
        currentDialog?.dismiss()
        currentDialog = null
    }
}




