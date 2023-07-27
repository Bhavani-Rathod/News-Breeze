package com.bhavani.newsbreeze

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RvItemClickListener(context: Context, recyclerview: RecyclerView, private val listner: OnRecyclerClickListener):RecyclerView.SimpleOnItemTouchListener() {
    interface OnRecyclerClickListener{
        fun onclick(view: View, position:Int)
        fun onLongClick(v:View,position: Int)
    }

    private val gestureDetector= GestureDetectorCompat(context,object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {

            val childView=recyclerview.findChildViewUnder(e.x,e.y)!!
            listner.onclick(childView,recyclerview.getChildAdapterPosition(childView))
            return true
        }

        override fun onLongPress(e: MotionEvent) {

            val childView=recyclerview.findChildViewUnder(e.x,e.y)!!
            listner.onLongClick(childView,recyclerview.getChildAdapterPosition(childView))
        }
    })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if(rv.findChildViewUnder(e.x,e.y)!=null) {
            return gestureDetector.onTouchEvent(e)
        }
        return false
    }
}