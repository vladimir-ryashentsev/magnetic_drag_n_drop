package ru.zengalt.draganddropsuspect

import android.content.ClipData
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StandardDDSample : AppCompatActivity() {

    private val paper by lazy { findViewById<ImageView>(R.id.paper) }
    private val trash by lazy { findViewById<ImageView>(R.id.trash) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paper.tag = "paper"
        trash.tag = "trash"
        trash.setOnDragListener(TrashDragListener(
                R.drawable.trash_open,
                R.drawable.trash_closed))
        trash.setOnClickListener { paper!!.visibility = VISIBLE }
        paper.setOnLongClickListener { view ->
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = MyDragShadowBuilder(view)
            view.startDrag(data, shadowBuilder, view, 0)
            view.visibility = INVISIBLE
            true
        }
    }

    private class MyDragShadowBuilder(view: View?) : DragShadowBuilder(view) {

        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            Log.d("BLABLA", "onProvideShadowMetrics")
//            view?.let {
//                outShadowSize.set(view.width, view.height)
//                outShadowTouchPoint.set(outShadowSize.x / 2, outShadowSize.y / 2)
//            }
            super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
        }

        override fun onDrawShadow(canvas: Canvas?) {
            Log.d("BLABLA", "onDrawShadow")
            super.onDrawShadow(canvas)
        }

    }

    private class TrashDragListener(private val enterShape: Int, private val normalShape: Int) : OnDragListener {
        private var hit = false
        override fun onDrag(v: View, event: DragEvent): Boolean {
            val containerView = v as ImageView
            val draggedView = event.localState as ImageView
            return when (event.action) {
                DragEvent.ACTION_DRAG_LOCATION -> {
                    Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION")
                    true
                }
                DragEvent.ACTION_DRAG_STARTED -> {
                    Log.d(TAG, "onDrag: ACTION_DRAG_STARTED")
                    hit = false
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED")
                    containerView.setImageResource(enterShape)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d(TAG, "onDrag: ACTION_DRAG_EXITED")
                    containerView.setImageResource(normalShape)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    Log.d(TAG, "onDrag: ACTION_DROP")
                    hit = true
                    draggedView.post { draggedView.visibility = GONE }
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d(TAG, "onDrag: ACTION_DRAG_ENDED")
                    containerView.setImageResource(normalShape)
                    v.setVisibility(VISIBLE)
                    if (!hit) {
                        draggedView.post { draggedView.visibility = VISIBLE }
                    }
                    true
                }
                else -> true
            }
        }

        companion object {
            private const val TAG = "TrashDragListener"
        }

    }
}