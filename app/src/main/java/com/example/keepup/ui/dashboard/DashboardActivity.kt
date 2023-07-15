package com.example.keepup.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepup.R
import com.example.keepup.data.db.AppDatabase
import com.example.keepup.databinding.ActivityMainBinding
import com.example.keepup.network.respository.NetworkRepository
import com.example.keepup.network.viewmodel.NetworkViewModel
import com.example.keepup.network.viewmodel.NetworkViewModelProviderFactory
import com.example.keepup.ui.adapter.NewsAdapter
import com.example.keepup.ui.adapter.NewsAdapter.OnClickListener
import com.example.keepup.ui.newsItem.NewsActivity
import com.example.keepup.ui.saved.SavedItemsActivity
import com.example.keepup.utils.Resource

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: NetworkViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter

    val TAG = "DashboardActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_background_color)

        initialize()
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {


        }

        binding.savedImg.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@DashboardActivity, SavedItemsActivity::class.java))
        })

        //subscribe to live data
        viewModel.topHeadLines.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    //check null
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Companion.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.topHeadLinesNewsPage == totalPages
                        if (isLastPage) {
                            binding.newsRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                        Toast.makeText(
                            this@DashboardActivity,
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun initialize() {
        val repository = NetworkRepository(AppDatabase(this@DashboardActivity))
        val viewModelProviderFactory = NetworkViewModelProviderFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NetworkViewModel::class.java)
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { newsDataItem, actionItem ->

            when (actionItem) {
                "share" -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Checkout this article from KeepUp\n\n" + newsDataItem.url)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
                else -> {
                    if (newsDataItem.isRestricted) {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Subscription coming soon",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@NewsAdapter
                    }

                    val intent = Intent(this@DashboardActivity, NewsActivity::class.java)
                    intent.putExtra("newsItem", newsDataItem)
                    startActivity(intent)
                }
            }

        }
        binding.newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            addOnScrollListener(this@DashboardActivity.scrollListener as RecyclerView.OnScrollListener)

        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            //manually calculating payout numbers for pagination
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Companion.QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getTopHeadlines("in")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    companion object {
        const val QUERY_PAGE_SIZE = 20
    }
}