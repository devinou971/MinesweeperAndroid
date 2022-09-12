package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.slider.Slider

class CustomGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_game)

        val colsSlider : Slider = findViewById(R.id.colsSlider)
        val rowsSlider : Slider = findViewById(R.id.rowsSlider)

        colsSlider.valueTo = intent.getIntExtra(NB_COLS, 10).toFloat()
        rowsSlider.valueTo = intent.getIntExtra(NB_ROWS, 10).toFloat()
        colsSlider.value = colsSlider.valueTo
        rowsSlider.value = rowsSlider.valueTo

        val gameButton = findViewById<Button>(R.id.startGameButton)
        gameButton.setOnClickListener{
            startGame()
        }
    }

    private fun startGame() {
        val colsSlider : Slider = findViewById(R.id.colsSlider)
        val rowsSlider : Slider = findViewById(R.id.rowsSlider)
        val bombsSlider : Slider = findViewById(R.id.bombsSlider)

        val nbCols = colsSlider.value.toInt()
        val nbRows = rowsSlider.value.toInt()
        val nbBombs = (nbRows * nbCols * (bombsSlider.value/100)).toInt()

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra(NB_BOMBS, nbBombs)
            putExtra(NB_COLS, nbCols)
            putExtra(NB_ROWS, nbRows)
        }
        startActivity(intent)
    }
}