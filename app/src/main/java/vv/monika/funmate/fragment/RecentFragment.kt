package vv.monika.funmate.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import vv.monika.funmate.adapter.HistoryAdapter
import vv.monika.funmate.databinding.FragmentRecentBinding
import vv.monika.funmate.model.ScoreViewModel


class RecentFragment : Fragment() {

    private lateinit var binding: FragmentRecentBinding
    private val scoreVM: ScoreViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecentBinding.inflate(layoutInflater, container, false)

       lifecycleScope.launchWhenStarted {
           scoreVM.score.collect { score ->
               binding.totalCoin.text = "$score"
           }
       }

//        dummy data
        val withDrawStatus = listOf("Pending", "Success", "Failed")
//        need to change color as well on success failure and pending
        val totalCoin = listOf("1000", "20000", "300")
        val dateTime = listOf("12/09/25-12:03", "2/09/25-11:23", "22/02/25-2:53")
        val adapter = HistoryAdapter(withDrawStatus = withDrawStatus, totalCoin = totalCoin, dataAndTime = dateTime )

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter



        return binding.root
    }


}