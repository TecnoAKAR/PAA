package cucerdariancatalin.chess
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.apperger.Ajedrez.Companion.TAG
import com.example.apperger.R
import kotlin.math.max
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private  val scaleFactor=1.0f
    private  var originX:Float=20f
    private  var originY:Float=200f
    private  var cellSide:Float=130f
    private val darkGrey =Color.parseColor("#BBBBBB")
    private val lightGrey =Color.parseColor("#EEEEEE")
    private  val imgResIDslMarker= setOf(
            R.drawable.bishop_black,
            R.drawable.bishop_white,
            R.drawable.king_black,
            R.drawable.king_white,
            R.drawable.knight_black,
            R.drawable.knight_white,
            R.drawable.rook_black,
            R.drawable.rook_white,
            R.drawable.pawn_black,
            R.drawable.pawn_white,
            R.drawable.queen_black,
            R.drawable.queen_white
    )
    private  val bitmaps= mutableMapOf<Int, Bitmap>()
    private  val paint = Paint()
    private var fromCol:Int=-1
    private var fromRow:Int=-1
    private var movingPieceX=-1f
    private var movingPieceY=-1f
    private var movingPieceBitmap: Bitmap?=null
    private var movingPiece:ChessPiece?=null

    var chessDeligate:ChessDeligate?=null

    init {
        loadBitmaps()
    }


    override fun onDraw(canvas: Canvas?) {

    canvas?:return

        val chessBoardSide= min(width, height) *scaleFactor
        cellSide=chessBoardSide/8f
        originX=(width-chessBoardSide)/2f
        originY=(height-chessBoardSide)/2f

    drawChessBoard(canvas)
    drawPieces(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec,widthMeasureSpec)
        val smaller= min(heightMeasureSpec  ,widthMeasureSpec )
        return setMeasuredDimension(smaller,smaller)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?: return false
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                fromCol=((event.x-originX)/cellSide).toInt()
                fromRow=7 - ((event.y-originY)/cellSide).toInt()



               chessDeligate?.pieceAt(Square(fromCol,fromRow))?.let {
                   movingPiece=it
                    movingPieceBitmap=bitmaps[it.resId]
                }

            }
            MotionEvent.ACTION_MOVE ->{
                movingPieceX=event.x
                movingPieceY=event.y
                invalidate()
            }
            MotionEvent.ACTION_UP ->{
                val col=((event.x-originX)/cellSide).toInt()
                val row=7 - ((event.y-originY)/cellSide).toInt()
                Log.d(TAG,"from ($fromCol, $fromRow) to  at (${col},${row})")
                chessDeligate?.movePiece(Square(fromCol,fromRow), Square(col, row))


                movingPiece=null
                movingPieceBitmap=null


            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas){
        for(row in 0..7){
            for(col in 0..7){

                chessDeligate?.pieceAt(Square(col,row))?.let{
                    if(it!=movingPiece){
                        drawPieceAt(canvas,col,row,it.resId)
                    }
                }

            }
        }

        movingPieceBitmap?.let {
            canvas.drawBitmap(it, null, RectF(movingPieceX-cellSide/2,movingPieceY-cellSide/2,movingPieceX+cellSide/2,movingPieceY+cellSide/2),paint)
        }

    }


    private fun drawPieceAt(canvas: Canvas, col:Int, row:Int, resId:Int){
        val bitmap=bitmaps[resId]!!
        canvas.drawBitmap(bitmap, null, RectF(originX+col*cellSide,originY+(7-row)*cellSide,originX+(col+1)*cellSide,originY+((7-row)+1)*cellSide),paint)
    }

    private fun loadBitmaps(){
        imgResIDslMarker.forEach{
            bitmaps[it]=BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawChessBoard(canvas:Canvas)
    {
        for(row in 0..7)
        {
            for (col in 0..7)
            {
                drawSquareAt(canvas,col,row,(col+row)%2==1)
                }
        }
    }
    private fun drawSquareAt(canvas: Canvas,col:Int,row: Int, isDark:Boolean){
        paint.color = if(isDark) darkGrey else lightGrey
        canvas.drawRect(originX +col*cellSide ,originY +row*cellSide,originX+(col+1)*cellSide,originY+(row+1)*cellSide, paint)

    }
}