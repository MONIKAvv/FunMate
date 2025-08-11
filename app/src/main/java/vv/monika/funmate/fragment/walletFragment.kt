package vv.monika.funmate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vv.monika.funmate.R
import vv.monika.funmate.databinding.ActivityRadeemCodeBinding
import vv.monika.funmate.databinding.FragmentWalletBinding


class walletFragment : Fragment() {
//    we are binding here wallet for now here in place of real one
   private lateinit var binding: ActivityRadeemCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityRadeemCodeBinding.inflate(layoutInflater,container,false)





        return binding.root
    }

}