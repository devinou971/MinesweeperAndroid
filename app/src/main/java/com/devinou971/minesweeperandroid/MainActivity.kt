package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

const val NB_ROWS = "nbRows"
const val NB_COLS = "nbCols"
const val NB_BOMBS = "nbBombs"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun goToMenu(view: View) {
        // In order to change Activity, We need to do this :
        val intent = Intent(this, MenuActivity::class.java).apply {  }
        startActivity(intent)
    }
}