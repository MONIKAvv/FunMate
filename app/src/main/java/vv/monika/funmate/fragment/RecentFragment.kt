package vv.monika.funmate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vv.monika.funmate.R
import vv.monika.funmate.databinding.FragmentRecentBinding


class RecentFragment : Fragment() {

    private lateinit var binding: FragmentRecentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecentBinding.inflate(layoutInflater, container, false)

//        dummy data



        return binding.root
    }


}