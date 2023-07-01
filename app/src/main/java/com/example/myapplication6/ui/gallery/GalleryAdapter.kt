package com.example.myapplication6.ui.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication6.R
import com.example.myapplication6.databinding.GalleryItemRecyclerviewBinding

class GalleryAdapter(private val context: Context, private val dataList : MutableList<Image>) : RecyclerView.Adapter<GalleryAdapter.Holder>() {
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapter.Holder {
        val binding = GalleryItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: GalleryAdapter.Holder, position: Int) {
        holder.bind(dataList[position].name,dataList[position].url.toUri(), context)
    }

    inner class Holder(private val binding: GalleryItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val img = binding.gridItemImg
        private val name = binding.gridItemName

        fun bind(text:String, url: Uri?, context: Context){
            if(url != null) {
                img.setImageURI(url)
                name.text = text
            }else{
                img.setImageResource(R.mipmap.ic_launcher_round)
                name.text = "text"
            }
        }
    }

}