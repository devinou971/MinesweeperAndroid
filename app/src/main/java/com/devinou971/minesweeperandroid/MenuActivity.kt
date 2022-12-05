package com.devinou971.minesweeperandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.storageclasses.GameDataDAO

class MenuActivity : AppCompatActivity() {

    private var appDatabase: AppDatabase? = null
    private var gameDataDAO: GameDataDAO? = null

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

        findViewById<ImageButton>(R.id.parameterButton).setOnClickListener {
            goToParameter()
        }


        val highscoreViews = mutableListOf<TextView>()
        highscoreViews.add(findViewById(R.id.bestScoreEasy))
        highscoreViews.add(findViewById(R.id.bestScoreNormal))
        highscoreViews.add(findViewById(R.id.bestScoreHard))

        Thread{
            accessDatabase()
            for(i in 0..highscoreViews.size){
                val bestScore = gameDataDAO?.getBestTimeForDifficulty(i)
                if(bestScore != null){
                    val minutes = bestScore.time / 60
                    val seconds = bestScore.time - minutes * 60

                    runOnUiThread{
                        highscoreViews[i].text = resources.getString(R.string.highscore, minutes, seconds)
                    }
                }
            }

        }.start()
    }

    private fun goToParameter(){
        val intent = Intent(this, ParametersActivity::class.java).apply {  }
        startActivity(intent)
    }

    private fun gotToGame(view: View) {
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
                    putExtra(DIFFICULTY, 0)
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
                    putExtra(DIFFICULTY, 1)
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
                    putExtra(DIFFICULTY, 2)
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

    private fun accessDatabase(){
        appDatabase = AppDatabase.getAppDataBase(this)
        gameDataDAO = appDatabase?.gameDataDAO()
    }
}