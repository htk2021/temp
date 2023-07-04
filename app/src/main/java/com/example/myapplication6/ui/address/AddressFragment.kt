package com.example.myapplication6.ui.address

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication6.R
import com.example.myapplication6.databinding.FragmentAddressBinding
import org.json.JSONArray
import com.example.myapplication6.ui.address.DetailFragment


class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //val profileList = ArrayList<Profile>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(AddressViewModel::class.java)

        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val profileList = ArrayList<Profile>()

        val jsonString = requireActivity().assets.open("simpledata.json").reader().readText()
        Log.d("JSON STR", jsonString)

        // 2. JSONArray 로 파싱
        val jsonArray = JSONArray(jsonString)
        Log.d("jsonArray", jsonArray.toString())

        // 3. JSONArray 순회: 인덱스별 JsonObject 취득후, key에 해당하는 value 확인
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)

            val img=jsonObject.getString("img")
            val id = jsonObject.getString("id")
            val language = jsonObject.getString("language")

            Log.d("jsonObject", jsonObject.toString())
            Log.d("json_id_language", "$id $language")

            profileList.add(Profile(img, id, language, "add"))
        }

        val adapter = AddressAdapter(requireContext(), profileList)
        adapter.setOnCancelClickListener { position ->
            profileList.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, profileList.size)
        }

        adapter.setOnItemClickListener(object : AddressAdapter.OnItemClickListener {
            override fun onItemClick(address: AddressItem) {
                showDetail(address)
            }
        })

        binding.rv.adapter = adapter //AddressAdapter(requireContext(), profileList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        return root
    }

    private fun showDetail(address: AddressItem) {
        val detailFragment = DetailFragment()
        val args = Bundle().apply {
            putString("name", address.text_name)
            putString("additionalInfo", address.text_additional_info)
        }
        detailFragment.arguments = args

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, detailFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}