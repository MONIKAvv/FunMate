package vv.monika.funmate.fragment

import android.content.Intent
import android.view.animation.Animation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.Toast
import vv.monika.funmate.AlertType
import vv.monika.funmate.AlphabetFunActivity
import vv.monika.funmate.BigvsSmallActivity
import vv.monika.funmate.CheckInActivity
import vv.monika.funmate.CustomAlert
import vv.monika.funmate.GameActivity
import vv.monika.funmate.InviteActivity
import vv.monika.funmate.MatchFunActivity
import vv.monika.funmate.PromoActivity
import vv.monika.funmate.R
import vv.monika.funmate.SoundMatchActivity
import vv.monika.funmate.databinding.FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(layoutInflater, container,false)

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
            startActivity(Intent(requireContext(), InviteActivity::class.java))
        }
        binding.promo.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            startActivity(Intent(requireContext(), PromoActivity::class.java))
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

}