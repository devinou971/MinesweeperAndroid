package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

const val NB_ROWS = "nbRows"
const val NB_COLS = "nbCols"
const val NB_BOMBS = "nbBombs"
const val CELL_SIZE = "cellSize"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToMenu(view:View) {
        val intent = Intent(this, MenuActivity::class.java).apply {  }
        startActivity(intent)
    }
}