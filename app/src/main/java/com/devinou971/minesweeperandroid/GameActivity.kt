package com.devinou971.minesweeperandroid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard
import com.devinou971.minesweeperandroid.customViews.GameView

class GameActivity : AppCompatActivity() {
    companion object{
        const val DEFAULT_CELL_HEIGHT = 100 // TODO : Make it so the size of the cells depend on the number of cell we want in the canvas
        const val DEFAULT_CELL_WIDTH = 100 // We don't want static sizes as it changes depending on the resolution of the screen
    }

    private lateinit var gameBoard : MinesweeperBoard
    private lateinit var gameView : GameView
    private lateinit var labelNbFlagsRemaining : TextView

    private lateinit var playerWin : ConstraintLayout
    private lateinit var playerLose : ConstraintLayout

    private var nbBombs = 0
    private var nbRows = 0
    private var nbCols = 0

    enum class Mode {
        REVEAL, FLAG
    }

    private var mode : Mode = Mode.REVEAL

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        // --------- GETTING THE DIFFERENT VALUES SENT FROM OTHER ACTIVITIES ---------
        nbBombs = intent.getIntExtra(NB_BOMBS, 10)
        nbRows = intent.getIntExtra(NB_ROWS, 10)
        nbCols = intent.getIntExtra(NB_COLS, 10)

        // --------- FETCHING ALL THE VIEWS ---------
        gameView = findViewById(R.id.gameView)
        playerWin = findViewById(R.id.playerWin)
        playerLose = findViewById(R.id.playerLose)
        labelNbFlagsRemaining = findViewById(R.id.nbFlagsRemaining)

        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)

        val modeSwitchButton = findViewById<ImageView>(R.id.switchmode)


        // --------- CREATE ON CLICK EVENT FOR CANVAS ---------
        gameView.setOnTouchListener { _, motionEvent : MotionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_UP){
                gridClickedEvent(motionEvent.x, motionEvent.y)
            }
            return@setOnTouchListener true
        }

        // --------- ONCLICK EVENT FOR THE "REPLAY" BUTTON

        findViewById<ImageView>(R.id.reloadBoard).setOnClickListener {replay()}
        findViewById<Button>(R.id.replayButton).setOnClickListener{replay()}
        findViewById<Button>(R.id.replayButton2).setOnClickListener{replay()}

        findViewById<Button>(R.id.returnMenuButton).setOnClickListener { goToMenu() }

        // --------- ONCLICK EVENT TO SWITCH BETWEEN FLAG AND REVEAL MODE ---------

        modeSwitchButton.setBackgroundResource(R.drawable.flagicon)

        modeSwitchButton.setOnClickListener {
            if(mode == Mode.REVEAL){
                mode = Mode.FLAG
                modeSwitchButton.setBackgroundResource(R.drawable.bombicon)
            }
            else {
                mode = Mode.REVEAL
                modeSwitchButton.setBackgroundResource(R.drawable.flagicon)
            }
        }


        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        gameView.gameBoard = gameBoard

        findViewById<Button>(R.id.anotherChance).setOnClickListener { revive() }
    }

    private fun gridClickedEvent(x: Float, y: Float){
        if(mode == Mode.REVEAL){
            gameView.reveal(x, y)
            if(gameBoard.won()){
                Toast.makeText(this, "You win GG!", Toast.LENGTH_LONG).show()
                gameView.visibility = GONE
                playerWin.visibility = VISIBLE
            }
            else if (gameBoard.gameOver){
                Toast.makeText(this, "Gameover", Toast.LENGTH_LONG).show()
                gameView.visibility = GONE
                playerLose.visibility = VISIBLE
            }
        }
        else if (mode == Mode.FLAG){
            gameView.flag(x, y)
            labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        }
    }

    private fun replay(){
        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)
        playerWin.visibility = GONE
        playerLose.visibility = GONE
        gameView.visibility = VISIBLE

        gameView.gameBoard = gameBoard

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        gameView.drawGrid()
    }

    private fun revive(){
        gameBoard.gameOver = false
        gameBoard.revive()

        playerWin.visibility = GONE
        playerLose.visibility = GONE
        gameView.visibility = VISIBLE

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        gameView.drawGrid()
    }

    private fun goToMenu(){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}