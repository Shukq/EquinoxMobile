package com.quinox.mobile.ui.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.quinox.mobile.R

class LessonsFragment : Fragment() {

    private lateinit var lessonsViewModel: LessonsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lessonsViewModel =
            ViewModelProviders.of(this).get(LessonsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_lessons, container, false)
        val textView: TextView = root.findViewById(R.id.text_tools)
        lessonsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}