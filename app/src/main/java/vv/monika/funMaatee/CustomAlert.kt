package vv.monika.funMaatee

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

object CustomAlert {

    fun showCustomAlert(
        context: Context,
        type: AlertType,
        title: String,
        description: String,
        onNextClick: (() -> Unit)? = null,
        onCloseClick: (() -> Unit)? = null// Optional callback for next button
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.activity_custom_alert, null)
        val dialogBuilder = AlertDialog.Builder(context).setView(dialogView).setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()

        // Access views directly from dialogView
        val icon = dialogView.findViewById<ImageView>(R.id.alert_icon)
        val textTitle = dialogView.findViewById<TextView>(R.id.alert_title)
        val message = dialogView.findViewById<TextView>(R.id.alert_description)
        val closeBtn = dialogView.findViewById<ImageView>(R.id.close_alert)
        val nextBtn = dialogView.findViewById<Button>(R.id.next_btn)

        // Set values
        textTitle.text = title
        message.text = description

        if (type == AlertType.WRONG || type == AlertType.CONGRATULATION) {
            nextBtn.visibility = View.GONE
        } else {
            nextBtn.visibility = View.VISIBLE
        }

        when (type) {
            AlertType.WRONG -> icon.setImageResource(R.drawable.wrong_icon)
            AlertType.CORRECT -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.START -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.APPROVAL -> icon.setImageResource(R.drawable.wrong_icon)
            AlertType.CONGRATULATION -> icon.setImageResource(R.drawable.correct_icon)
        }

        closeBtn.setOnClickListener {
            alertDialog.dismiss()
            onCloseClick?.invoke()
        }

        nextBtn.setOnClickListener {
            alertDialog.dismiss()  // Close dialog first
            onNextClick?.invoke()   // Then call the callback function
        }
    }
}