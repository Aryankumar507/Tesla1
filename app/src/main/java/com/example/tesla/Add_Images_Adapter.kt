package com.example.tesla

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tesla.databinding.ImageItemBinding

class Add_Images_Adapter(private val context: Context,private val images:List<UserImage>): RecyclerView.Adapter<Add_Images_Adapter.ImageViewHolder>() {
    class ImageViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.imageView2)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
      val view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: Add_Images_Adapter.ImageViewHolder, position: Int) {
      val currentImage = images[position]
        Glide.with(context).load(currentImage.imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size

    }
}