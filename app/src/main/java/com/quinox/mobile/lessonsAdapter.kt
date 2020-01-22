package com.quinox.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.quinox.domain.entities.ContentfulClass

class lessonsAdapter(val callback : (ContentfulClass) -> Unit) : RecyclerView.Adapter<lessonsAdapter.lessonVH>(){
    private var listClasses: List<ContentfulClass> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): lessonVH {
        return lessonVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_class_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listClasses.size
    }

    override fun onBindViewHolder(holder: lessonVH, position: Int) {
        val lesson = listClasses[position]
        holder.title.text = lesson.title
        val number = "Clase "+ (position + 1).toString()
        holder.classNumber.text = number
        holder.cv.setOnClickListener {
            callback(lesson)
        }
    }

    fun setLessonList(lessonList: List<ContentfulClass>)
    {
        this.listClasses = lessonList
        notifyDataSetChanged()
    }

    class lessonVH(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.primary_text)
        val classNumber: TextView = view.findViewById(R.id.supporting_text)
        val cv: CardView = view.findViewById(R.id.cv_lessonsRecycler)
    }
}