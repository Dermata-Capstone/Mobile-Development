package com.dicoding.dermata.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dermata.databinding.ItemArticleBinding
import com.dicoding.dermata.response.WebsitesItem

class ArticleAdapter(
    private val websites: List<WebsitesItem>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val website = websites[position]
        holder.bind(website)
    }

    override fun getItemCount() = websites.size

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(website: WebsitesItem) {

            binding.titleTextView.text = website.title

            // Load the image using an image loading library like Glide or Picasso
            Glide.with(binding.imageView.context)
                .load(website.image) // Assuming mediaCover holds the image URL
                .into(binding.imageView)

            // Set the click listener for the item
            binding.root.setOnClickListener {
                website.link?.let { it1 -> onClick(it1) } // Assuming link is the URL to open when the item is clicked
            }
        }
    }
}

