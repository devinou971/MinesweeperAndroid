package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViewById<Button>(R.id.easyLevelButton).setOnClickListener {
            gotToGame(it)
        }

        findViewById<Button>(R.id.normalLevelButton).setOnClickListener {
            gotToGame(it)
        }

        findViewById<Button>(R.id.hardLevelButton).setOnClickListener {
            gotToGame(it)
        }

        findViewById<Button>(R.id.customLevelButon).setOnClickListener {
            gotToGame(it)
        }
    }

    fun gotToGame(view: View) {
        val availableHeight = (window.decorView.height * 0.80).toInt()
        val availableWidth = window.decorView.width
        val nbCols = 10
        val cellSize = availableWidth / nbCols
        val nbRows = availableHeight / cellSize
        when(view.id){
            R.id.easyLevelButton -> {
                val nbBombs = (nbRows * nbCols * 0.15).toInt()
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(NB_BOMBS, nbBombs)
                    putExtra(NB_COLS, nbCols)
                    putExtra(NB_ROWS, nbRows)
                    putExtra(CELL_SIZE, cellSize)
                }
                startActivity(intent)
            }
            R.id.normalLevelButton -> {
                val nbBombs = (nbRows * nbCols * 0.22).toInt()
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(NB_BOMBS, nbBombs)
                    putExtra(NB_COLS, nbCols)
                    putExtra(NB_ROWS, nbRows)
                    putExtra(CELL_SIZE, cellSize)
                }
                startActivity(intent)
            }
            R.id.hardLevelButton -> {
                val nbBombs = (nbRows * nbCols * 0.38).toInt()
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(NB_BOMBS, nbBombs)
                    putExtra(NB_COLS, nbCols)
                    putExtra(NB_ROWS, nbRows)
                    putExtra(CELL_SIZE, cellSize)
                }
                startActivity(intent)
            }
            R.id.customLevelButon -> {
                val intent = Intent(this, CustomGameActivity::class.java).apply {
                    putExtra(NB_COLS, nbCols)
                    putExtra(NB_ROWS, nbRows)
                }
                startActivity(intent)
            }
        }
    }
}