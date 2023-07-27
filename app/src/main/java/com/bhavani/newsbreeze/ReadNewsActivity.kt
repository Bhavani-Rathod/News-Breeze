package com.bhavani.newsbreeze

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bhavani.newsbreeze.databinding.ActivityReadNewsBinding
import com.bhavani.newsbreeze.models.NewsArticle
import com.squareup.picasso.Picasso

class ReadNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val newsList = intent.getSerializableExtra("PHOTO_TRANSFER") as NewsArticle
        val buttonText = intent.getStringExtra("save")
        val scrollImage = findViewById<ImageView>(R.id.scrollimageView)

        val author = findViewById<TextView>(R.id.author)
        binding.intotextView.text = newsList.title
        author.text = newsList.author


        if (buttonText == "Saved") {
            binding.detailsavebutton.text = buttonText

            binding.detailSave.setBackgroundResource(R.drawable.detail_bookmark_24)
        }

        binding.detailsavebutton.setOnClickListener {
            if (buttonText != "Saved") {
                binding.detailsavebutton.setText(R.string.saved)
                binding.detailSave.setBackgroundResource(R.drawable.detail_bookmark_24)

            }
        }
        Picasso.get().load(newsList.urlToImage)
            .error(R.drawable.news_image)
            .placeholder(R.drawable.news_image).into(scrollImage)
        webData(newsList.url.toString())

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun webData(murl: String) {
        val webContent = findViewById<WebView>(R.id.webcontent)
        webContent.settings.loadsImagesAutomatically = true
        webContent.settings.javaScriptEnabled = true
        webContent.settings.setSupportZoom(true)
        webContent.settings.domStorageEnabled = true
        webContent.settings.displayZoomControls = false
        webContent.settings.builtInZoomControls = true
        webContent.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webContent.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url != "Invalid") {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        if (murl != "Invalid") webContent.loadUrl(murl)

    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }
}