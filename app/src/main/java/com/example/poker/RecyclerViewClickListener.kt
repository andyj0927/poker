package com.example.poker

import android.view.View

interface RecyclerViewClickListener {
    fun onClick(view: View, position: Int)
}