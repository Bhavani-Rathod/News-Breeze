package com.bhavani.newsbreeze.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhavani.newsbreeze.R
import com.bhavani.newsbreeze.ReadNewsActivity
import com.bhavani.newsbreeze.models.NewsArticle
import com.squareup.picasso.Picasso
import java.util.regex.Pattern

class MainAdapter(private var newslist: List<NewsArticle>, private val context: Context):
    RecyclerView.Adapter<MainAdapter.ImageViewHolder>() {
    class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var thumbNail = v.findViewById<ImageView>(R.id.thumbnail)
        var title = v.findViewById<TextView>(R.id.tittle)
        var date = v.findViewById<TextView>(R.id.date)
        var description = v.findViewById<TextView>(R.id.description)
        val saveButton = v.findViewById<Button>(R.id.savebutton)
        val readButton = v.findViewById<Button>(R.id.readbutton)
        val indicator = v.findViewById<ImageView>(R.id.savedBtn)

    }

    var savedList = ArrayList<NewsArticle>()
    var beforeSearch=ArrayList<NewsArticle>()
    var checklist: Array<String>?=null

    private var newsList: List<NewsArticle> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newslist_content, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val cdata = newslist[position]
        Picasso.get().load(cdata.urlToImage)
            .error(R.drawable.news_image)
            .placeholder(R.drawable.news_image).into(holder.thumbNail)
        holder.title.text = cdata.title
        val fullDate = Pattern.compile("T").split(cdata.publishedAt.toString())
        holder.date.text = fullDate[0]
        holder.description.text = cdata.description


        if(checklist?.get(position) =="false"){holder.indicator.setImageResource(R.drawable.saved_icon)

            holder.indicator.setBackgroundResource(R.drawable.rounded)
            holder.saveButton.setText(R.string.saved)
    }

        holder.saveButton.setOnClickListener {

            if (checklist?.get(position) == "true") {
                savedList.add(cdata)
                holder.saveButton.setText(R.string.saved)
                holder.indicator.setImageResource(R.drawable.saved_icon)
                holder.indicator.setBackgroundResource(R.drawable.rounded)
                checklist!![position]= "false"

            }

        }
        holder.readButton.setOnClickListener {
            val intent = Intent(context, ReadNewsActivity::class.java)
            intent.putExtra("PHOTO_TRANSFER", cdata)
            intent.putExtra("position",position)
            intent.putExtra("save",holder.saveButton.text)
            context.startActivity(intent)
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return if (newslist.isNotEmpty()) newslist.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun onChange(newList: List<NewsArticle>, Checklist:Array<String>, check:Int) {
        if (check==1){ beforeSearch = newList as ArrayList<NewsArticle> }
        newslist = newList
        notifyDataSetChanged()
        checklist=Checklist

    }
    fun onChange2(NewList: List<NewsArticle>){
        newslist = NewList
        notifyDataSetChanged()
    }

    fun setNews(prevList:ArrayList<NewsArticle>){
        newslist=prevList
    }
    fun getUnTouchList():ArrayList<NewsArticle>{
        return beforeSearch
    }
    fun getNewsList(): ArrayList<NewsArticle> {
        return newslist as ArrayList<NewsArticle>

    }

    fun getSavedlist(): ArrayList<NewsArticle> {
        return savedList

    }
    fun getCheckList():Array<String>{
        return checklist as Array<String>
    }

    fun sortByDate() {
        newsList = newsList.sortedByDescending { it.publishedAt }
        notifyDataSetChanged()
    }

}