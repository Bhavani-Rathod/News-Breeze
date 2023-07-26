package com.bhavani.newsbreeze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhavani.newsbreeze.adapter.MainAdapter
import com.bhavani.newsbreeze.models.News
import com.bhavani.newsbreeze.models.NewsArticle
import com.bhavani.newsbreeze.utils.NullCheck
import com.bhavani.newsbreeze.utils.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import android.widget.*
import java.util.*

private var flag = 0

class MainActivity : AppCompatActivity() {

    var recyclerviewadapter = MainAdapter(ArrayList(), this)
    private var cachelistnews: ArrayList<NewsArticle>? = null
    val copynewslist = ArrayList<NewsArticle>()
    var originalnewslist = ArrayList<NewsArticle>()
    private lateinit var popupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val searchbar = findViewById<SearchView>(R.id.Search_bar_saved)
        val recycleview = findViewById<RecyclerView>(R.id.savedrecycleview)
        val savelistbutton = findViewById<ImageButton>(R.id.savedBtn)

        recycleview.layoutManager = LinearLayoutManager(this)
        recycleview.adapter = recyclerviewadapter

        getNews(RetrofitInstance.newsInstance.getHeadlines("in", 1))
        search(searchbar)

        savelistbutton.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedNews::class.java)
            intent.putExtra("SAVED_LIST", recyclerviewadapter.getSavedlist())
            startActivity(intent)
        }
        val filterButton = findViewById<ImageButton>(R.id.filter)

        filterButton.setOnClickListener { v -> showPopupMenu(v) }

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        // Inflate the popup menu layout
        popupMenu.menuInflater.inflate(R.menu.menu_filter, popupMenu.menu)

        // Set a click listener for the popup menu items
        popupMenu.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }

        // Show the popup menu
        popupMenu.show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("key", recyclerviewadapter.getSavedlist())
        outState.putSerializable("serial", recyclerviewadapter.getNewsList())
        outState.putSerializable("untouch", recyclerviewadapter.getUnTouchList())
        outState.putStringArray("checklist", recyclerviewadapter.getCheckList())
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val savedlist = savedInstanceState.getSerializable("key") as ArrayList<NewsArticle>
        recyclerviewadapter.savedList = savedlist
        cachelistnews = savedInstanceState.getSerializable("serial") as ArrayList<NewsArticle>
        originalnewslist = savedInstanceState.getSerializable("untouch") as ArrayList<NewsArticle>
        val checklist = savedInstanceState.getStringArray("checklist") as Array<String>
        recyclerviewadapter.beforesearch = originalnewslist
        recyclerviewadapter.onChange(cachelistnews!!, checklist, 0)
        flag = 1

    }


    @Suppress("UNCHECKED_CAST")
    fun getNews(news:Call<News>) {
        if (flag == 0) {
            flag = 1
            news.enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    val newsA = response.body()
                    if (newsA != null) {
                        newsA.articles = NullCheck.check(newsA.articles as ArrayList<NewsArticle>)
                        originalnewslist = newsA.articles as ArrayList<NewsArticle>
                        val savedlist = recyclerviewadapter.getSavedlist()
                        val array = makeBooleanarray(newsA.articles.size)
                        recyclerviewadapter.onChange(newsA.articles, array, 1)
                        recyclerviewadapter.savedList = savedlist
                    }

                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun makeBooleanarray(size: Int): Array<String> {
        val array = arrayOfNulls<String>(size)
        for (i in array.indices) {
            array[i] = "true"
        }
        return array as Array<String>
    }


    private fun search(searchbar: SearchView) {
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    copynewslist.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    originalnewslist.forEach {
                        if ((it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true) || (it.description?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true)
                        ) {
                            copynewslist.add(it)
                        }
                    }
                    recyclerviewadapter.onChange2(copynewslist)
                } else {
                    recyclerviewadapter.onChange2(
                        originalnewslist
                    )
                }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_date -> {
                // Handle "Sort by Date" click here
                recyclerviewadapter.sortByDate()
                return true
            }
            // Handle other menu items if any
        }
        return super.onOptionsItemSelected(item)
    }
}