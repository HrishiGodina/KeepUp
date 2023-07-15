package com.example.keepup.ui.saved

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keepup.R
import com.example.keepup.data.db.AppDatabase
import com.example.keepup.databinding.ActivitySavedItemsBinding
import com.example.keepup.network.respository.NetworkRepository
import com.example.keepup.network.viewmodel.NetworkViewModel
import com.example.keepup.network.viewmodel.NetworkViewModelProviderFactory
import com.example.keepup.ui.adapter.NewsAdapter
import com.example.keepup.ui.newsItem.NewsActivity
import com.google.android.material.snackbar.Snackbar

class SavedItemsActivity : AppCompatActivity() {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel: NetworkViewModel
    private lateinit var binding: ActivitySavedItemsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySavedItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_background_color)

        val repository = NetworkRepository(AppDatabase(this@SavedItemsActivity))
        val viewModelProviderFactory = NetworkViewModelProviderFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NetworkViewModel::class.java)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this@SavedItemsActivity, NewsActivity::class.java)
            intent.putExtra("newsItem", it)
            startActivity(intent)
        }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, //direction
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // swipe direction
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteSavedNewsDataItem(article)
                //execute undo Snackbar
                Snackbar.make(binding.root, " Article Deleted Successfully ", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.saveNewsItem(article)
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.savedRecyclerView)
        }

        viewModel.getSavedNewsDataItems().observe(this, Observer { newsDataItems ->
            if (!newsDataItems.isEmpty()) {
                newsAdapter.differ.submitList(newsDataItems)
                binding.savedRecyclerView.visibility = View.VISIBLE
                binding.noDataImg.visibility = View.GONE
            } else {
                binding.savedRecyclerView.visibility = View.GONE
                binding.noDataImg.visibility = View.VISIBLE
            }

        })
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.savedRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SavedItemsActivity)
        }
    }
}