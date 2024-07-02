package com.example.tesla

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tesla.databinding.ImageItemBinding

class Add_Images_Adapter(val urls:List<String>): RecyclerView.Adapter<Add_Images_Adapter.ImageViewHolder>() {
    class ImageViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.imageView2)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_add_images,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Add_Images_Adapter.ImageViewHolder, position: Int) {
        val url = urls[position]
        Glide.with(holder.itemView).load(url).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return urls.size

    }
}