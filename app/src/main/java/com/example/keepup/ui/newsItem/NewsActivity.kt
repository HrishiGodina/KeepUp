package com.example.keepup.ui.newsItem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.example.keepup.data.db.AppDatabase
import com.example.keepup.data.model.NewsDataItem
import com.example.keepup.databinding.ActivityNewsBinding
import com.example.keepup.network.respository.NetworkRepository
import com.example.keepup.network.viewmodel.NetworkViewModel
import com.example.keepup.network.viewmodel.NetworkViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NetworkRepository(AppDatabase(this))
        val viewModelProviderFactory = NetworkViewModelProviderFactory(application, repository)
        val viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NetworkViewModel::class.java)

        val newsDataItem = intent.getSerializableExtra("newsItem") as? NewsDataItem
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(newsDataItem?.url.toString())
        }

        binding.fab.setOnClickListener {
            viewModel.saveNewsItem(newsDataItem!!)
            Snackbar.make(binding.root, " Article Saved Successfully! ", Snackbar.LENGTH_SHORT).show()
        }
    }
}