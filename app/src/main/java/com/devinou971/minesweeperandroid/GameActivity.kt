package com.devinou971.minesweeperandroid

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
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
import androidx.core.graphics.scale
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard
import com.devinou971.minesweeperandroid.customViews.GameView

class GameActivity : AppCompatActivity() {
    // ------------- ADDED -------------
    private lateinit var bombIcon : Bitmap
    private lateinit var flagIcon : Bitmap
    private lateinit var tileIcon : Bitmap

    private val paints : MutableList<Paint> = mutableListOf(Paint(0).apply {
        color = Color.BLUE
        textSize = 50f
    }, Paint(0).apply {
        color = Color.GREEN
        textSize = 50f
    }, Paint(0).apply {
        color = Color.RED
        textSize = 50f
    }, Paint(0).apply {
        color = Color.rgb(0,0,127)
        textSize = 50f
    }, Paint(0).apply {
        color = Color.rgb(127,0,0)
        textSize = 50f
    }, Paint(0).apply {
        color = Color.rgb(255,192, 203)
        textSize = 50f
    }, Paint(0).apply {
        color = Color.MAGENTA
        textSize = 50f
    }, Paint(0).apply {
        color = Color.CYAN
        textSize = 50f
    }, Paint(0).apply {
        color = Color.YELLOW
        textSize = 50f
    })

    // --------------------------

    private lateinit var gameBoard : MinesweeperBoard
    private lateinit var gameView : GameView
    private lateinit var labelNbFlagsRemaining : TextView

    private lateinit var playerWin : ConstraintLayout
    private lateinit var playerLose : ConstraintLayout

    private var nbBombs = 0
    private var nbRows = 0
    private var nbCols = 0
    private var cellSize : Int = 0

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
        cellSize = intent.getIntExtra(CELL_SIZE, 100)

        bombIcon = BitmapFactory.decodeResource(resources, R.drawable.bombicon).scale(cellSize, cellSize, false)
        flagIcon = BitmapFactory.decodeResource(resources, R.drawable.flagicon).scale(cellSize, cellSize, false)
        tileIcon = BitmapFactory.decodeResource(resources, R.drawable.emptytile).scale(cellSize, cellSize, false)

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
        gameView.setZOrderOnTop(true)
        gameView.holder.setFormat(PixelFormat.TRANSPARENT)

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
        findViewById<Button>(R.id.anotherChance).setOnClickListener { revive() }

        gameView.viewTreeObserver.addOnWindowFocusChangeListener {
            drawGrid()
        }

    }



    private fun gridClickedEvent(x: Float, y: Float){
        if(mode == Mode.REVEAL){
            reveal(x, y)
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
            flag(x, y)
            labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        }
    }

    private fun replay(){
        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)
        playerWin.visibility = GONE
        playerLose.visibility = GONE
        gameView.visibility = VISIBLE

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        drawGrid()
    }

    private fun revive(){
        gameBoard.gameOver = false
        gameBoard.revive()

        playerWin.visibility = GONE
        playerLose.visibility = GONE
        gameView.visibility = VISIBLE

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        drawGrid()
    }

    private fun goToMenu(){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    // ------------- ADDED -------------

    fun reveal(x : Float, y : Float){
        val trueX = (x / cellSize).toInt()
        val trueY = (y / cellSize).toInt()

        if(trueX in 0 until gameBoard.nbCols && trueY in 0 until gameBoard.nbRows){
            gameBoard.reveal(Point(trueX, trueY))
            drawGrid()
        }
    }

    fun flag(x : Float, y: Float){
        val trueX = (x / cellSize).toInt()
        val trueY = (y / cellSize).toInt()

        if(trueX in 0 until gameBoard.nbCols && trueY in 0 until gameBoard.nbRows){
            gameBoard.flag(Point(trueX, trueY))
            drawGrid()
        }
    }

    fun drawGrid(){
        val can = gameView.holder.lockCanvas()
        can.drawColor(this.applicationContext.getColor(R.color.bgColor), PorterDuff.Mode.CLEAR)

        val textXOffset = cellSize / 3
        val textYOffset = cellSize * 2/3

        for (y in 0 until gameBoard.grid.size){
            for (x in 0 until gameBoard.grid[0].size){
                when(val res = gameBoard.grid[y][x].toString()){
                    "B" ->{
                        can.drawBitmap(bombIcon, x*cellSize.toFloat() , y*cellSize.toFloat(), Paint(0))
                    }
                    "F" -> {
                        can.drawBitmap(flagIcon, x*cellSize.toFloat() , y*cellSize.toFloat(), Paint(0))
                    }
                    "#" -> {
                        can.drawBitmap(tileIcon, x*cellSize.toFloat(), y*cellSize.toFloat(), Paint(0))
                    }
                    "0" -> {
                        // We don't display when there are no bombs
                    }
                    else -> {
                        can.drawText(res,
                            x*cellSize + textXOffset.toFloat(),
                            y*cellSize + textYOffset.toFloat(),
                            paints[res.toInt() -1])
                    }
                }
            }
        }
        gameView.holder.unlockCanvasAndPost(can)
    }

}