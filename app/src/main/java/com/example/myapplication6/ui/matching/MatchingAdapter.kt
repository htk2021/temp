package com.example.myapplication6.ui.matching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication6.databinding.MatchingRecyclerviewBinding

class MatchingAdapter(private val context: Context, val profileList : ArrayList<Team>) : RecyclerView.Adapter<MatchingAdapter.Holder>() {
    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchingAdapter.Holder {
        val binding = MatchingRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: MatchingAdapter.Holder, position: Int) {
        holder.team_name.text = profileList[position].team_name
        holder.name_1.text = profileList[position].name_1
        holder.name_2.text = profileList[position].name_2
        holder.name_3.text = profileList[position].name_3

        val resId_1 = context.resources.getIdentifier(profileList[position].image_1, "drawable", context.packageName)
        holder.image_1.setImageResource(resId_1)
        val resId_2 = context.resources.getIdentifier(profileList[position].image_2, "drawable", context.packageName)
        holder.image_2.setImageResource(resId_2)
        val resId_3 = context.resources.getIdentifier(profileList[position].image_3, "drawable", context.packageName)
        holder.image_3.setImageResource(resId_3)

        if (holder.name_3.text!="") {
            holder.setImageViewVisibility(View.VISIBLE)
        } else {
            holder.setImageViewVisibility(View.GONE)
        }
    }

    inner class Holder(val binding: MatchingRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val team_name = binding.teamName
        val name_1 = binding.name1
        val name_2 = binding.name2
        val name_3 = binding.name3
        val image_1 = binding.image1
        val image_2 = binding.image2
        val image_3 = binding.image3
        fun setImageViewVisibility(visibility: Int) {
            binding.name3.visibility = visibility
            binding.image3.visibility = visibility
        }
    }
}