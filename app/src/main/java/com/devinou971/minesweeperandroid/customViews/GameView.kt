package com.devinou971.minesweeperandroid.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.scale
import com.devinou971.minesweeperandroid.GameActivity
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : android.view.SurfaceView(context, attrs, defStyleAttr) {
    // TODO : Make the icons lighter
    private val bombIcon : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bombicon).scale(GameActivity.DEFAULT_CELL_WIDTH, GameActivity.DEFAULT_CELL_HEIGHT, false)
    private val flagIcon : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.flagicon).scale(GameActivity.DEFAULT_CELL_WIDTH, GameActivity.DEFAULT_CELL_HEIGHT, false)
    private val tileIcon : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.emptytile).scale(GameActivity.DEFAULT_CELL_WIDTH, GameActivity.DEFAULT_CELL_HEIGHT, false)

    // TODO : Make the colors lighter to see the numbers better
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

    private var cellWidth : Int = 100
    private var cellHeight : Int = 100

    lateinit var gameBoard : MinesweeperBoard

    private var ww = 0
    private var hh = 0
    private var xOffset = 0f
    private var yOffset = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
    /*
    private fun drawLines(canvas : Canvas){
        val gridWidth = gameBoard.grid[0].size
        val gridHeight = gameBoard.grid.size

        for(i in 0 .. gridWidth){
            canvas.drawLine((i*GameActivity.CELL_WIDTH).toFloat(), 0f, (i*GameActivity.CELL_WIDTH).toFloat(), (gridHeight*GameActivity.CELL_HEIGHT).toFloat() , outlinePaint)
        }
        for(i in 0 .. gridHeight){
            canvas.drawLine(0f, (i*GameActivity.CELL_HEIGHT).toFloat(), (gridWidth*GameActivity.CELL_WIDTH).toFloat(), (i*GameActivity.CELL_HEIGHT).toFloat(), outlinePaint)
        }
    }
    */
    fun reveal(x : Float, y : Float){
        val trueX = ((x - xOffset) / GameActivity.DEFAULT_CELL_WIDTH).toInt()
        val trueY = ((y - yOffset) / GameActivity.DEFAULT_CELL_HEIGHT).toInt()

        if(trueX in 0 until gameBoard.nbCols && trueY in 0 until gameBoard.nbRows){
            gameBoard.reveal(Point(trueX, trueY))
            drawGrid()
        }
    }

    fun flag(x : Float, y: Float){
        val trueX = ((x - xOffset) / GameActivity.DEFAULT_CELL_WIDTH).toInt()
        val trueY = ((y - yOffset) / GameActivity.DEFAULT_CELL_HEIGHT).toInt()

        if(trueX in 0 until gameBoard.nbCols && trueY in 0 until gameBoard.nbRows){
            gameBoard.flag(Point(trueX, trueY))
            drawGrid()
        }
    }

    fun drawGrid(){
        val can = holder.lockCanvas()
        can.drawColor(context.getColor(R.color.bgColor), PorterDuff.Mode.CLEAR)

        //val textXOffset = GameActivity.CELL_WIDTH/2 - paints[0].textSize/2
        //val textYOffset = GameActivity.CELL_HEIGHT/2
        val textXOffset = GameActivity.DEFAULT_CELL_WIDTH / 3
        val textYOffset = GameActivity.DEFAULT_CELL_HEIGHT * 2/3

        for (y in 0 until gameBoard.grid.size){
            for (x in 0 until gameBoard.grid[0].size){
                when(val res = gameBoard.grid[y][x].toString()){
                    "B" ->{
                        can.drawBitmap(bombIcon, x*GameActivity.DEFAULT_CELL_WIDTH +xOffset, y*GameActivity.DEFAULT_CELL_HEIGHT + yOffset, Paint(0))
                    }
                    "F" -> {
                        can.drawBitmap(flagIcon, x*GameActivity.DEFAULT_CELL_WIDTH +xOffset, y*GameActivity.DEFAULT_CELL_HEIGHT + yOffset, Paint(0))
                    }
                    "#" -> {
                        can.drawBitmap(tileIcon, x*GameActivity.DEFAULT_CELL_WIDTH +xOffset, y*GameActivity.DEFAULT_CELL_HEIGHT + yOffset, Paint(0))
                    }
                    "0" -> {
                        // We don't display when there are no bombs
                    }
                    else -> {

                        can.drawText(res,
                            x*GameActivity.DEFAULT_CELL_WIDTH + textXOffset + xOffset,
                            y*GameActivity.DEFAULT_CELL_HEIGHT + textYOffset + yOffset,
                            paints[res.toInt() -1])
                    }
                }
            }
        }
        holder.unlockCanvasAndPost(can)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // We override the sizeChanged event to get the "reel" width and height of the View
        super.onSizeChanged(w, h, oldw, oldh)
        ww = w
        hh = h

        xOffset = w/2 - GameActivity.DEFAULT_CELL_WIDTH*gameBoard.nbCols/2f
        yOffset = h/2 - GameActivity.DEFAULT_CELL_HEIGHT*gameBoard.nbRows/2f

        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }
}