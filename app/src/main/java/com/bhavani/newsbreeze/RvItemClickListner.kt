package com.bhavani.newsbreeze

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RvItemClickListner(context: Context, recyclerview: RecyclerView, private val listner: OnRecyclerClickListner):RecyclerView.SimpleOnItemTouchListener() {
    interface OnRecyclerClickListner{
        fun onclick(v: View, position:Int)
        fun onLongClick(v:View,position: Int)
    }

    private val gesturedetector= GestureDetectorCompat(context,object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {

            val childview=recyclerview.findChildViewUnder(e.x,e.y)!!
            listner.onclick(childview,recyclerview.getChildAdapterPosition(childview))
            return true
        }

        override fun onLongPress(e: MotionEvent) {

            val childview=recyclerview.findChildViewUnder(e.x,e.y)!!
            listner.onLongClick(childview,recyclerview.getChildAdapterPosition(childview))
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if(rv.findChildViewUnder(e.x,e.y)!=null) {
            return gesturedetector.onTouchEvent(e)
        }
        return false
    }
}