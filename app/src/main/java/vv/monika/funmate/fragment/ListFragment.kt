package vv.monika.funmate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vv.monika.funmate.R
import vv.monika.funmate.databinding.FragmentListBinding


class ListFragment : Fragment() {

private lateinit var binding: FragmentListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentListBinding.inflate(layoutInflater, container,false)




        return binding.root
    }


}