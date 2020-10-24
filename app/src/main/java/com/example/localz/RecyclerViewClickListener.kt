package com.example.localz

import android.view.View

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View,cart:CartDetails)
}