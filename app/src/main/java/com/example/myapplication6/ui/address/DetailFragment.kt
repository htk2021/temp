package com.example.myapplication6.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication6.R
import com.example.myapplication6.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val name = arguments?.getString("name")
        val age = arguments?.getString("age")
        val additionalInfo = arguments?.getString("additionalInfo")

        val textName: TextView = binding.textName
        textName.text = name

        val textAge: TextView = binding.textAge
        textAge.text = age

        val textAdditionalInfo: TextView = binding.textAdditionalInfo
        textAdditionalInfo.text = additionalInfo
        binding.goBack.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.navigation_home)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}