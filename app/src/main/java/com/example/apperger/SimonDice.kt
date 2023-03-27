package com.example.apperger

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView


class SimonDice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simon_dice)


        val drag1ImageView = findViewById<ImageView>(R.id.drag1ImageView)
        drag1ImageView.setOnLongClickListener(longClickListener)
        val drag2ImageView = findViewById<ImageView>(R.id.drag2ImageView)
        drag2ImageView.setOnLongClickListener(longClickListener)
        val drag3ImageView = findViewById<ImageView>(R.id.drag3ImageView)
        drag3ImageView.setOnLongClickListener(longClickListener)

        val target1ImageView = findViewById<ImageView>(R.id.target1ImageView)
        target1ImageView.setOnDragListener(dragListener)
        val target2ImageView = findViewById<ImageView>(R.id.target2ImageView)
        target2ImageView.setOnDragListener(dragListener)
        val target3ImageView = findViewById<ImageView>(R.id.target3ImageView)
        target3ImageView.setOnDragListener(dragListener)
    }

    private class MyDragShadowBuilder(val v: View) : View.DragShadowBuilder(v) {

        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            size.set(view.width, view.height)
            touch.set(view.width / 2, view.height / 2)
        }
        override fun onDrawShadow(canvas: Canvas) {
            v.draw(canvas)
        }
    }

    private val longClickListener = View.OnLongClickListener { v ->
        val item = ClipData.Item(v.tag as? CharSequence)

        val dragData = ClipData(
            v.tag as CharSequence,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item
        )

        val myShadow = MyDragShadowBuilder(v)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v.startDragAndDrop(dragData, myShadow,null,0)
        } else {
            v.startDrag(dragData, myShadow,null,0)
        }

        true
    }

    private val dragListener = View.OnDragListener { v, event ->
        val receiverView: ImageView = v as ImageView

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {

                true
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                if(event.clipDescription.label == receiverView.tag as String) {
                    receiverView.setColorFilter(Color.GREEN)

                } else {
                    receiverView.setColorFilter(Color.RED)

                }
                v.invalidate()
                true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                true

            DragEvent.ACTION_DRAG_EXITED -> {
                if(event.clipDescription.label == receiverView.tag as String) {
                    receiverView.setColorFilter(Color.YELLOW)

                    v.invalidate()
                }
                true
            }

            DragEvent.ACTION_DROP -> {

                true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }

            else -> {
                false
            }
        }
    }
}