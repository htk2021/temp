package com.example.myapplication6.ui.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication6.databinding.ItemRecyclerviewBinding

class AddressAdapter(val profileList : ArrayList<Profile>) : RecyclerView.Adapter<AddressAdapter.Holder>() {
    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: AddressAdapter.Holder, position: Int) {
        holder.name.text = profileList[position].name
        holder.age.text = profileList[position].age
    }

    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.rvName
        val age = binding.rvAge
    }
}