package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import com.devinou971.minesweeperandroid.extensions.nextTo

class Slot {
    var isRevealed : Boolean = false
    private set
    var isBomb : Boolean = false
    var nbBombs : Int = 0
    var isFlagged = false
    var position = Point(0,0)

    fun reveal() {
        this.isRevealed = true
    }

    override fun toString(): String {
        var result = "#"
        if(isFlagged)
            result = "F"
        if(isRevealed)
            result = if(!isBomb) "$nbBombs" else "B"
        return result
    }

    fun nextTo(o:Slot) : Boolean{
        return this.position.nextTo(o.position)
    }

    fun getUnseenNeighbors(grid : MutableList<MutableList<Slot>>) : MutableList<Slot>{
        val neighbors = mutableListOf<Slot>()
        for(line in grid){
            neighbors.addAll(line.filter { x->this.nextTo(x)&&!x.isRevealed })
        }

        return neighbors
    }

    fun xRayView() : String{
        return if(isBomb) "B" else "$nbBombs"
    }

    fun hide(){
        isRevealed = false
    }
}