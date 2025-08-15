// File: CustomAlert.kt
package vv.monika.funmate

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

object CustomAlert {

    fun showCustomAlert(context: Context, type: AlertType, title: String, description: String) {
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

        // Set values
        textTitle.text = title
        message.text = description

        when (type) {
            AlertType.WRONG -> icon.setImageResource(R.drawable.wrong_icon)
            AlertType.CORRECT -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.APPROVAL -> icon.setImageResource(R.drawable.correct_icon)
            AlertType.CONGRATULATION -> icon.setImageResource(R.drawable.correct_icon)
        }

        closeBtn.setOnClickListener {
            alertDialog.dismiss()
        }

    }
}
