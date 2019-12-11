package com.quinox.mobile.ui.lessons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.mobile.R

class UnitAdapter(val callback : (List<ContentfulSection>) -> Unit) : RecyclerView.Adapter<UnitAdapter.UnitVH>(){
    private var listUnits: List<ContentfulUnit> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitVH {
        return UnitVH(LayoutInflater.from(parent.context).inflate(R.layout.recycler_unit_row,parent,false))
    }

    override fun getItemCount(): Int {
        return listUnits.size
    }

    override fun onBindViewHolder(holder: UnitVH, position: Int) {
        val unit = listUnits[position]
        holder.title.text = unit.title
        holder.description.text = unit.description
        holder.btnSections.isEnabled = unit.sections.isNotEmpty()
        holder.btnSections.setOnClickListener {
            callback(unit.sections)
        }
    }

    fun setUnitList(unitList: List<ContentfulUnit>)
    {
        this.listUnits = unitList
        notifyDataSetChanged()
    }

    class UnitVH(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.primary_text)
        val description: TextView = view.findViewById(R.id.supporting_text)
        val btnSections: Button = view.findViewById(R.id.action_button_1)
    }
}