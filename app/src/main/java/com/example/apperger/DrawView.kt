package com.raghav.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


class DrawView(c: Context, attributeSet: AttributeSet) : View(c, attributeSet) {

    private val TOUCH_TOLERANCE = 4f
    private var mX = 0f
    private var mY = 0f
    private lateinit var mPath: Path

    private var mPaint: Paint = Paint()

    private val paths = mutableListOf<Stroke>()
    private var currentColor = 0
    private var strokeWidth = 0
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private val mBitmapPaint = Paint(Paint.DITHER_FLAG)

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.alpha = 0xff
    }

    fun init(height: Int, width: Int) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)

        currentColor = Color.BLACK
        strokeWidth = 20
    }

    fun setColor(color: Int) {
        currentColor = color
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
    }

    fun undo() {
        if (paths.size != 0) {
            paths.removeAt(paths.size - 1)
            invalidate()
        }
    }

    fun save(): Bitmap? {
        return mBitmap
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        val backgroundColor = Color.WHITE
        mCanvas!!.drawColor(backgroundColor)
        for (fp in paths) {
            mPaint.color = fp.color
            mPaint.strokeWidth = fp.strokeWidth.toFloat()
            mCanvas.drawPath(fp.path, mPaint)
        }
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }


    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val fp = Stroke(currentColor, strokeWidth, mPath)
        paths.add(fp)

        mPath.reset()
        mPath.moveTo(x, y)

        mX = x
        mY = y
    }


    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - mX)
        val dy = abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }


    private fun touchUp() {
        mPath.lineTo(mX, mY)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

}