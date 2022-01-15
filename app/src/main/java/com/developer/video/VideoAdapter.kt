package com.developer.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.developer.scopedstorage.R
import com.developer.video.model.VideoModel

class VideoAdapter(val listener: (Int,VideoModel) -> Unit) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private val mList = mutableListOf<VideoModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : AppCompatTextView = itemView.findViewById(R.id.txtVideoName)
        val imgThumbnail: AppCompatImageView = itemView.findViewById(R.id.ivVideosThumbnail)
        val imgPlayVideo: AppCompatImageView = itemView.findViewById(R.id.ivVideoItemPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_list_layout,parent,false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: VideoAdapter.ViewHolder, position: Int) {

        val video = mList[position]

        holder.name.text = video.name
        video.thumbnails?.let {
            holder.imgThumbnail.setImageBitmap(it)
        }

        holder.itemView.setOnClickListener {
            listener(position,video)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun submitList(mList:List<VideoModel>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }
}