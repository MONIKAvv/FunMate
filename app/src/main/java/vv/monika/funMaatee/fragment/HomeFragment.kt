package vv.monika.funMaatee.fragment

import TopSheetDialogFragment
import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import com.google.firebase.auth.FirebaseAuth
import vv.monika.funMaatee.AlertType
import vv.monika.funMaatee.AlphabetFunActivity
import vv.monika.funMaatee.BigvsSmallActivity
import vv.monika.funMaatee.CheckInActivity
import vv.monika.funMaatee.Congrats
import vv.monika.funMaatee.CustomAlert
import vv.monika.funMaatee.GameActivity
import vv.monika.funMaatee.InviteActivity
import vv.monika.funMaatee.LoginActivity
import vv.monika.funMaatee.MatchFunActivity
import vv.monika.funMaatee.PromoActivity
import vv.monika.funMaatee.SoundMatchActivity
import vv.monika.funMaatee.databinding.FragmentHomeBinding
import vv.monika.funMaatee.databinding.FragmentTopSheetBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.checkIn.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), CheckInActivity::class.java))
//            alert
        }
        binding.vigSmall.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), BigvsSmallActivity::class.java))
        }
        binding.sound.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), SoundMatchActivity::class.java))
        }
        binding.mathFun.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), MatchFunActivity::class.java))
        }
        binding.alphabet.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), AlphabetFunActivity::class.java))
        }
        binding.games.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), GameActivity::class.java))
        }
        binding.invite.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                startActivity(Intent(requireContext(), InviteActivity::class.java))
            } else {
              showLogin()
            }


        }
        binding.promo.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(currentUser != null){
                startActivity(Intent(requireContext(), PromoActivity::class.java))
            }else{
                showLogin()
            }

        }




        return binding.root
    }

    private fun createScaleAnimation(): ScaleAnimation {
        return ScaleAnimation(
            1f, 1.2f,
            1f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 150
            fillAfter = true
            repeatMode = Animation.REVERSE
            repeatCount = 1
        }
    }

    private fun showLogin(){
        TopSheetDialogFragment().show(parentFragmentManager, "TopSheet")
//        TopSheetDialogFragment().show(supportFragmentManager, "TopSheet") for activity
    }

}