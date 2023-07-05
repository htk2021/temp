package com.example.myapplication6.ui.address

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication6.databinding.ItemRecyclerviewBinding
import com.example.myapplication6.ui.matching.Characteristics

class AddressAdapter(private val context: Context, val profileList : ArrayList<Characteristics>) : RecyclerView.Adapter<AddressAdapter.Holder>() {

    private var onCancelClickListener: ((position: Int) -> Unit)? = null


    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: AddressAdapter.Holder, position: Int) {
        val resId = context.resources.getIdentifier(profileList[position].img, "drawable", context.packageName)
        holder.name.text = profileList[position].name
        holder.age.text = profileList[position].age
        holder.img.setImageResource(resId)

        holder.cancelButton.setOnClickListener {
            onCancelClickListener?.invoke(position)
        }



    }

    fun setOnCancelClickListener(listener: (position: Int) -> Unit) {
        onCancelClickListener = listener
    }



    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.rvName
        val age = binding.rvAge
        val img = binding.rvImage
        val cancelButton = binding.btnCancel

    }
}