package com.swbg.mlivestreaming.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.ui.mine.MineViewModel

class GameFragment : Fragment() {

    private lateinit var notificationsViewModel: MineViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel = ViewModelProvider(this).get(MineViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_game, container, false)
        val textView: TextView = root.findViewById(R.id.text_game)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}