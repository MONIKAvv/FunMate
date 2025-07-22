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
import vv.monika.funmate.InviteActivity
import vv.monika.funmate.PromoActivity
import vv.monika.funmate.R
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
            Toast.makeText(requireContext(), "check in toast", Toast.LENGTH_SHORT).show()
        }
        binding.vigSmall.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            Toast.makeText(requireContext(), "Vig Small toast", Toast.LENGTH_SHORT).show()
        }
        binding.sound.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            Toast.makeText(requireContext(), "sound toast", Toast.LENGTH_SHORT).show()
        }
        binding.mathFun.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            Toast.makeText(requireContext(), "mathfun toast", Toast.LENGTH_SHORT).show()
        }
        binding.alphabet.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            Toast.makeText(requireContext(), "Alphabet toast", Toast.LENGTH_SHORT).show()
        }
        binding.games.setOnClickListener {
            it.startAnimation(createScaleAnimation())
            Toast.makeText(requireContext(), "Games toast", Toast.LENGTH_SHORT).show()
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