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
import com.bhavani.newsbreeze.databinding.ActivityMainBinding
import java.util.*

private var flag = 0

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var rvAdapter = MainAdapter(ArrayList(), this)
    private var cacheListNews: ArrayList<NewsArticle>? = null
    var originalNewsList = ArrayList<NewsArticle>()
    val copyNewsList = ArrayList<NewsArticle>()
    private lateinit var popUpMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        val searchbar = findViewById<SearchView>(R.id.Search_bar_saved)
        val recycleView = findViewById<RecyclerView>(R.id.savedrecycleview)

        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = rvAdapter

        getNews(RetrofitInstance.newsInstance.getHeadlines("in", 1))
        search(searchbar)

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedNews::class.java)
            intent.putExtra("SAVED_LIST", rvAdapter.getSavedlist())
            startActivity(intent)
        }
        val filterButton = findViewById<ImageButton>(R.id.filter)

        filterButton.setOnClickListener { v -> showPopupMenu(v) }

    }

    private fun search(searchbar: SearchView) {
        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    copyNewsList.clear()
                    val searchText = newText.lowercase(Locale.getDefault())
                    originalNewsList.forEach {
                        if ((it.title?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true) || (it.description?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true)
                        ) {
                            copyNewsList.add(it)
                        }
                    }
                    rvAdapter.onChange2(copyNewsList)
                } else {
                    rvAdapter.onChange2(
                        originalNewsList
                    )
                }
                return true
            }
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        popupMenu.menuInflater.inflate(R.menu.menu_filter, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item -> onOptionsItemSelected(item) }

        popupMenu.show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("key", rvAdapter.getSavedlist())
        outState.putSerializable("serial", rvAdapter.getNewsList())
        outState.putSerializable("untouch", rvAdapter.getUnTouchList())
        outState.putStringArray("checklist", rvAdapter.getCheckList())
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val savedList = savedInstanceState.getSerializable("key") as ArrayList<NewsArticle>
        rvAdapter.savedList = savedList
        cacheListNews = savedInstanceState.getSerializable("serial") as ArrayList<NewsArticle>
        originalNewsList = savedInstanceState.getSerializable("untouch") as ArrayList<NewsArticle>
        val checklist = savedInstanceState.getStringArray("checklist") as Array<String>
        rvAdapter.beforeSearch = originalNewsList
        rvAdapter.onChange(cacheListNews!!, checklist, 0)
        flag = 1

    }


    @Suppress("UNCHECKED_CAST")
    private fun getNews(news:Call<News>) {
        if (flag == 0) {
            flag = 1
            news.enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    val newsA = response.body()
                    if (newsA != null) {
                        newsA.articles = NullCheck.check(newsA.articles as ArrayList<NewsArticle>)
                        originalNewsList = newsA.articles as ArrayList<NewsArticle>
                        val savedlist = rvAdapter.getSavedlist()
                        val array = makeBooleanArray(newsA.articles.size)
                        rvAdapter.onChange(newsA.articles, array, 1)
                        rvAdapter.savedList = savedlist
                    }

                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "failed", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun makeBooleanArray(size: Int): Array<String> {
        val array = arrayOfNulls<String>(size)
        for (i in array.indices) {
            array[i] = "true"
        }
        return array as Array<String>
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sort_by_date -> {
                rvAdapter.sortByDate()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}