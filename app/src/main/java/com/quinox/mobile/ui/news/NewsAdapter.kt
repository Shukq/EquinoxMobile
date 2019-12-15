package com.quinox.mobile.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quinox.domain.entities.ContentfulNews
import com.quinox.mobile.R

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsVH>(){
    private var listNews: List<ContentfulNews> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        return NewsVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_news_row,parent,false))
    }

    override fun getItemCount(): Int {
        return listNews.size
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        val news = listNews[position]
        holder.title.text = news.title
        holder.header.text = news.header

    }

    fun setNewsList(newsList: List<ContentfulNews>)
    {
        this.listNews = newsList
        notifyDataSetChanged()
    }

    class NewsVH(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.primary_text)
        val header: TextView = view.findViewById(R.id.supporting_text)
    }
}