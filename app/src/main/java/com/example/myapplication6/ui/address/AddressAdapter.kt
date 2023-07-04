package com.example.myapplication6.ui.address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
        holder.kaist.text = if(profileList[position].kaist == "true") "kaist" else "not-kaist"
        holder.phone.text = profileList[position].phone
        holder.male.text = if(profileList[position].male == "true") "male" else "female"
        Glide.with(context)
            .load(resId) // 이미지의 URL 또는 리소스 ID
            .apply(RequestOptions.circleCropTransform()) // CircleCrop 변환기 적용
            .into(holder.img)
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