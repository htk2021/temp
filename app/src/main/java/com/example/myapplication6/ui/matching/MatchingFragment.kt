package com.example.myapplication6.ui.matching

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication6.databinding.FragmentMatchingBinding
import com.example.myapplication6.ui.matching.MatchingAdapter
import com.example.myapplication6.ui.matching.Profile
import org.json.JSONArray
import java.util.Collections.shuffle

class MatchingFragment : Fragment() {

    private var _binding: FragmentMatchingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(MatchingViewModel::class.java)

        _binding = FragmentMatchingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val profileList = ArrayList<Profile>()

        /*
        profileList.add(Profile("1조", "김병훈", "강은비", ""))
        profileList.add(Profile("2조", "박성빈", "박윤배", ""))
        profileList.add(Profile("3조", "남지현", "황태경", ""))
        profileList.add(Profile("1조", "김병훈", "강은비", ""))
        profileList.add(Profile("2조", "박성빈", "박윤배", ""))
        profileList.add(Profile("3조", "남지현", "황태경", ""))
        profileList.add(Profile("4조", "3인조", "3인조", "3인조"))
        */


        val list_student = mutableListOf<String>("송민지","황제욱","심수연","김태형","노태윤","김서경","유석원","김은수","김현호","신민영","김병훈","강은비","박성빈","박윤배","남지현","황태경","정지우","홍은빈","김서현")
        shuffle(list_student)
        for(i: Int in 0..14 step(2)){
            profileList.add(Profile((i/2+1).toString()+"조", list_student[i], list_student[i+1], ""))
        }
        profileList.add(Profile("9조", list_student[16], list_student[17], list_student[18]))

        binding.rv.adapter = MatchingAdapter(requireContext(), profileList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.showContentButton.setOnClickListener {
            binding.textNotifications.visibility = View.VISIBLE
            binding.rv.visibility = View.VISIBLE
            binding.showContentButton.visibility=View.INVISIBLE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}