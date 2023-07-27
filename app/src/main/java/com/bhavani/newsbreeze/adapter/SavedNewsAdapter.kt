package com.bhavani.newsbreeze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhavani.newsbreeze.R
import com.bhavani.newsbreeze.models.NewsArticle
import com.squareup.picasso.Picasso

class SavedNewsAdapter(private var newslist:List<NewsArticle>): RecyclerView.Adapter<SavedNewsAdapter.ImageViewHolder>() {

    class ImageViewHolder(v: View):RecyclerView.ViewHolder(v) {
        var thumbNail=v.findViewById<ImageView>(R.id.savedimage)
        var title=v.findViewById<TextView>(R.id.savedtitile)
        var date=v.findViewById<TextView>(R.id.saveddate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.saved_news,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val cdata=newslist[position]
        if(cdata.urlToImage==null){cdata.urlToImage="Invalid"}
        if(cdata.title==null){cdata.title="Title Not Available"}
        if(cdata.publishedAt==null){cdata.publishedAt="Date Not Available"}

        Picasso.get().load(cdata.urlToImage)
            .error(R.drawable.news_image)
            .placeholder(R.drawable.news_image).into(holder.thumbNail)
        holder.title.text=cdata.title
        holder.date.text=cdata.publishedAt
    }

    override fun getItemCount(): Int {
        return if(newslist.isNotEmpty()) newslist.size else 0
    }

    fun onChange( newlist:List<NewsArticle>){
        newslist=newlist
        notifyDataSetChanged()
    }

    fun getNews(position: Int): NewsArticle?{
        return if(newslist.isNotEmpty()) newslist[position] else null
    }
}