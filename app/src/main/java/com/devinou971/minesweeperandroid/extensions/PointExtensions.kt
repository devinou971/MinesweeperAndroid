package com.devinou971.minesweeperandroid.extensions



import android.graphics.Point

fun Point.nextTo(o:Point) : Boolean{
    return o.y in this.y-1..this.y+1 && o.x in this.x-1..this.x+1
}

fun Point.countNeighbors(l:MutableList<Point>) : Int {
    return l.count { x->this.nextTo(x) }
}

