package com.example.keepup.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.keepup.R
import com.example.keepup.data.model.NewsDataItem
import com.example.keepup.databinding.NewsItemLayoutBinding
import com.example.keepup.utils.BlurTransformation
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>() {

    inner class NewsItemViewHolder(itemView: NewsItemLayoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding = itemView
    }

    private val differCallback = object : DiffUtil.ItemCallback<NewsDataItem>() {
        override fun areItemsTheSame(oldItem: NewsDataItem, newItem: NewsDataItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsDataItem, newItem: NewsDataItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding =
            NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsDataItem = differ.currentList[position]

        holder.itemView.apply {
            if (newsDataItem.isRestricted)
                Glide.with(context)
                    .load(R.drawable.blur_thubnail)
                    .fitCenter()
                    .transform(BlurTransformation(context))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                    .apply(
                        RequestOptions().placeholder(R.drawable.default_thubnail)
                            .error(R.drawable.default_thubnail)
                    )
                    .into(holder.binding.newsImage)
            else
                Glide.with(context)
                    .load(newsDataItem.urlToImage)
                    .fitCenter()
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                    .apply(
                        RequestOptions().placeholder(R.drawable.default_thubnail)
                            .error(R.drawable.default_thubnail)
                    )
                    .into(holder.binding.newsImage)

            holder.binding.publisherNameTxt.text = newsDataItem.source?.name
            holder.binding.newsHeadingTxt.text =
                if (newsDataItem.isRestricted) context.getString(R.string.subscription_string) else newsDataItem.title
            holder.binding.publishedTimeTxt.text = getDays(newsDataItem.publishedAt)
            setOnClickListener {
                onItemClickListener?.let { it(newsDataItem) }
            }
        }
    }

    private fun getDays(publishedAt: String?): String {
        if (publishedAt?.isEmpty()!!)
            return "Today"

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

        val date = publishedAt?.let { simpleDateFormat.parse(it) }

        var diff = System.currentTimeMillis() - (date?.time ?: 0)

        diff = diff / 1000 / 60 / 60 / 24

        return if (diff > 0)
            String.format("%dD", diff)
        else
            "Today"
    }

    private var onItemClickListener: ((NewsDataItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (NewsDataItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}