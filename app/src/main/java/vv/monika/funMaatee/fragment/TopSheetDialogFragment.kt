import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import vv.monika.funMaatee.LoginActivity
import vv.monika.funMaatee.R

class TopSheetDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.TopSheetDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_top_sheet)

        dialog.findViewById<View>(R.id.login_btn)?.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            dismiss()
        }

        // set position to TOP
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.TOP)  // 👈 key line
        }

        return dialog
    }
}
