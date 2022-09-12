package com.devinou971.minesweeperandroid.classes

import android.graphics.Point
import com.devinou971.minesweeperandroid.extensions.countNeighbors
import com.devinou971.minesweeperandroid.extensions.nextTo
import java.sql.Time
import java.time.Instant
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class MinesweeperBoard(val nbRows : Int, val nbCols : Int, private val nbBombs : Int) {
    var nbFlags = nbBombs
    private set
    val grid = MutableList (nbRows) { MutableList (nbCols) {Slot()} }
    var gameOver = false
    private var isFirstTouch = true

    fun reveal(position : Point){
        if(!gameOver)
        {
            if(isFirstTouch){
                addBombsDynamicallyToGrid(position)
                isFirstTouch = false
            }


            val slot = grid[position.y][position.x]
            if(slot.isFlagged){
                return
            }
            if(slot.isBomb){
                gameOver = true
                slot.reveal()
                return
            }
            slot.reveal()
            if(slot.nbBombs == 0){
                val neighbors = slot.getUnseenNeighbors(grid)
                for (neighbor in neighbors){
                    if(!neighbor.isBomb && !neighbor.isFlagged &&!neighbor.isRevealed){
                        neighbor.reveal()
                        this.reveal(neighbor.position)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        var result = ""
        for(line in grid){
            result += line.joinToString(" ") + "\n"
        }
        return result
    }

    fun flag(position : Point) : Boolean {
        val slot = grid[position.y][position.x]

        if (!slot.isRevealed){
            if(slot.isFlagged) {
                nbFlags ++
                slot.isFlagged = false
            }
            else if (nbFlags>0){
                nbFlags --
                slot.isFlagged = true
            }
        }
        return slot.isFlagged
    }

    private fun addBombsDynamicallyToGrid(firstTouch:Point) {
        val bombs = mutableListOf<Point>()
        val numberGenerator = Random(Instant.now().epochSecond)
        for(i in 1..this.nbBombs){
            var x : Int
            var y : Int
            do {

                x = numberGenerator.nextInt(0 until nbCols)
                y = numberGenerator.nextInt(0 until nbRows)

            } while (Point(x,y).nextTo(firstTouch) || Point(x,y) in bombs)
            bombs.add(Point(x,y))
        }
        for (y in 0 until nbRows){
            for(x in 0 until nbCols){
                grid[y][x].position = Point(x, y)
                if (grid[y][x].position in bombs)
                    grid[y][x].isBomb = true
                else
                    grid[y][x].nbBombs = grid[y][x].position.countNeighbors(bombs)
            }
        }
    }

    fun xRayView() : String{
        var result = ""
        for(line in grid){
            for (slot in line){
                result += slot.xRayView() + " "
            }
            result += "\n"
        }
        return result
    }

    fun won() : Boolean{
        for (line in grid){
            for (slot in line){
                if(!(slot.isRevealed || slot.isBomb))
                    return false
            }
        }
        return true
    }

    fun revive(){
        gameOver = false
        for(line in grid){
            for(slot in line){
                if(slot.isBomb && slot.isRevealed){
                    slot.hide()
                }
            }
        }
    }
}