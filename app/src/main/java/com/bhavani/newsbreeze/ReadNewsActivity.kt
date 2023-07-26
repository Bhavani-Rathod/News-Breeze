package com.bhavani.newsbreeze

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bhavani.newsbreeze.models.NewsArticle
import com.squareup.picasso.Picasso

class ReadNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_news)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val newslist = intent.getSerializableExtra("PHOTO_TRANSFER") as NewsArticle
        val buttonText = intent.getStringExtra("save")
        val scrollimage = findViewById<ImageView>(R.id.scrollimageView)
        val intotextview = findViewById<TextView>(R.id.intotextView)
        val detailsave = findViewById<ImageButton>(R.id.detail_save)
        val author = findViewById<TextView>(R.id.author)
        intotextview.text = newslist.title
        author.text = newslist.author
        val savebutton = findViewById<Button>(R.id.detailsavebutton)
        if (buttonText == "Saved") {
            savebutton.text = buttonText

            detailsave.setBackgroundResource(R.drawable.detail_bookmark_24)
        }

        savebutton.setOnClickListener {
            if (buttonText != "Saved") {
                savebutton.setText(R.string.saved)
                detailsave.setBackgroundResource(R.drawable.detail_bookmark_24)

            }
        }
        Picasso.get().load(newslist.urlToImage)
            .error(R.drawable.news_image)
            .placeholder(R.drawable.news_image).into(scrollimage)
        webdata(newslist.url.toString())

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webdata(murl: String) {
        val webcontent = findViewById<WebView>(R.id.webcontent)
        webcontent.settings.loadsImagesAutomatically = true
        webcontent.settings.javaScriptEnabled = true
        webcontent.settings.setSupportZoom(true)
        webcontent.settings.domStorageEnabled = true
        webcontent.settings.displayZoomControls = false
        webcontent.settings.builtInZoomControls = true
        webcontent.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webcontent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url != "Invalid") {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        if (murl != "Invalid") webcontent.loadUrl(murl)


    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }
}