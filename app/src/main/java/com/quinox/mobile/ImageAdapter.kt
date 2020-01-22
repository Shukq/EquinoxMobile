package com.quinox.mobile

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(context: Context) : RecyclerView.Adapter<ImageAdapter.ImageVH>(){
    private var mDataset: List<String> = listOf()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageVH {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycler_image_row, p0, false)
        return ImageAdapter.ImageVH(v)
    }


    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun onBindViewHolder(p0: ImageVH, p1: Int) {
        val uri = mDataset[p1]
        Picasso.get().load(uri).error(R.drawable.equidad_color).into(p0.image)
        p0.itemView.setOnClickListener {
            imageIntent(p0.itemView.context,uri)
        }
    }

    class ImageVH(v: View) : RecyclerView.ViewHolder(v){
        var image: ImageView?= null

        init {
            image = v.findViewById(R.id.img_classDetailActivity)
        }
    }

    fun setAlbum(album:List<String>){
        mDataset = album
        notifyDataSetChanged()
    }

    private fun imageIntent(context : Context, url : String){
        val intent = Intent(context, ImageActivity::class.java)
        intent.putExtra("url",url)
        context.startActivity(intent)
    }

}