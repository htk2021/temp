package com.example.myapplication6.ui.address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication6.databinding.ItemRecyclerviewBinding

class AddressAdapter( private val context: Context, val profileList : ArrayList<Profile>) : RecyclerView.Adapter<AddressAdapter.Holder>() {

    private var onCancelClickListener: ((position: Int) -> Unit)? = null
    private var onItemClickListener: ((position: Int) -> Unit)? = null


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
        holder.kaist.text = profileList[position].kaist
        holder.phone.text = profileList[position].phone
        holder.male.text = profileList[position].male
        holder.img.setImageResource(resId)

        holder.cancelButton.setOnClickListener {
            onCancelClickListener?.invoke(position)
        }
        holder.img.setOnClickListener{
            onItemClickListener?.invoke(position)
        }


    }

    fun setOnCancelClickListener(listener: (position: Int) -> Unit) {
        onCancelClickListener = listener
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }


    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.rvName
        val male = binding.rvMale
        val kaist = binding.rvKaist
        val img = binding.rvImage
        val phone = binding.rvPhone
        val cancelButton = binding.btnCancel
        val item = binding.rvItem

    }
}