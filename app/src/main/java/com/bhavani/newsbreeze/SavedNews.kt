package com.bhavani.newsbreeze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhavani.newsbreeze.adapter.SavedNewsAdapter
import com.bhavani.newsbreeze.models.NewsArticle
import java.util.Locale

class SavedNews : AppCompatActivity(), RvItemClickListener.OnRecyclerClickListener {

    val copyNewsList = ArrayList<NewsArticle>()
    var originalnewslist = ArrayList<NewsArticle>()
    var savedListAdapter = SavedNewsAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_news_activity)

        val toolbar=findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val savedRecycleView = findViewById<RecyclerView>(R.id.savedrecycleview)

        savedRecycleView.layoutManager = LinearLayoutManager(this)
        savedRecycleView.addOnItemTouchListener(
            RvItemClickListener(
                this,
                savedRecycleView,
                this
            )
        )
        savedRecycleView.adapter = savedListAdapter

        val savedlist = intent.getSerializableExtra("SAVED_LIST") as ArrayList<NewsArticle>

        originalnewslist.addAll(savedlist)
        copyNewsList.addAll(originalnewslist)
        savedListAdapter.onChange(copyNewsList)

        search(findViewById(R.id.Search_bar_saved))
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun search(searchbar: SearchView) {
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    copyNewsList.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    originalnewslist.forEach {
                        if (it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
                            copyNewsList.add(it)
                        }
                    }
                    savedListAdapter.onChange(copyNewsList)
                } else {
                    savedListAdapter.onChange(originalnewslist)
                }
                return true
            }
        })
    }

    override fun onclick(view: View, position: Int) {
        val news = savedListAdapter.getNews(position)

        if (news != null) {
            val intent = Intent(this, ReadNewsActivity::class.java)
            intent.putExtra("PHOTO_TRANSFER", news)
            startActivity(intent)
        }
    }

    override fun onLongClick(v: View, position: Int) {

    }
}