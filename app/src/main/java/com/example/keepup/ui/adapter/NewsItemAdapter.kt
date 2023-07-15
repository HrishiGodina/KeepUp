package com.example.keepup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.keepup.R
import com.example.keepup.data.model.NewsDataItem
import com.example.keepup.databinding.NewsItemLayoutBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsItemAdapter(private val newsItems: ArrayList<NewsDataItem>, val context: Context) :
    RecyclerView.Adapter<NewsItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(newsItems.get(position), context)
    }

    override fun getItemCount(): Int {
        if (newsItems.isEmpty())
            return 0
        return newsItems.size
    }

    private var onItemClickListener:((NewsDataItem)->Unit)?=null

    fun setOnItemClickListener(listener: (NewsDataItem)->Unit){
        onItemClickListener= listener
    }

    class MyViewHolder(newsItemLayoutBinding: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(newsItemLayoutBinding.root) {

        private var binding = newsItemLayoutBinding;

        fun bind(newsDataItem: NewsDataItem, context: Context) {
            binding.newsHeadingTxt.text = newsDataItem.title
            binding.publisherNameTxt.text = newsDataItem.author
            binding.publishedTimeTxt.text = getDays(newsDataItem.publishedAt)

            Glide.with(context)
                .load(newsDataItem.urlToImage)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_thubnail)
                        .error(R.drawable.default_thubnail)
                )
                .into(binding.newsImage)

        }

        private fun getDays(publishedAt: String?): String {
            var days: String = ""

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

            val date = simpleDateFormat.parse(publishedAt)

            var diff = System.currentTimeMillis() - (date?.time ?: 0)

            diff = diff / 1000 / 60 / 60 / 24

            if (diff > 0)
                return String.format("%dD", diff)
            else
                return "Today"
        }
    }

}