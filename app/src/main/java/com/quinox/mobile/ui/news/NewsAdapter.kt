package com.quinox.mobile.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.quinox.domain.entities.ContentfulNews
import com.quinox.mobile.R

class NewsAdapter(val callback: (ContentfulNews) -> Unit) : RecyclerView.Adapter<NewsAdapter.NewsVH>(){
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
        holder.cv.setOnClickListener { callback(news) }

    }

    fun setNewsList(newsList: List<ContentfulNews>)
    {
        this.listNews = newsList
        notifyDataSetChanged()
    }

    class NewsVH(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.primary_text)
        val header: TextView = view.findViewById(R.id.supporting_text)
        val cv: CardView = view.findViewById(R.id.cv_recycler_news)
    }
}