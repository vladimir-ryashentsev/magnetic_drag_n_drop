package ru.zengalt.draganddropsuspect.dd

import android.graphics.Point
import android.view.MotionEvent
import android.view.View

class DragAndDrop(
    private val behavior: Behavior,
    draggingView: View,
    targetView: View,
    startingGlobalPoint: Point,
    private val listener: Listener
) {

    init {
        behavior.setViews(draggingView, targetView)
        val parent = draggingView.parent as View
        parent.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val touchPoint = Point(event.rawX.toInt(), event.rawY.toInt())
                behavior.onDrag(touchPoint)
            } else if (event.action == MotionEvent.ACTION_UP) {
                parent.setOnTouchListener(null)
                behavior.onFinish()
                listener.onDrop()
            }
            true
        }

        behavior.onStart(startingGlobalPoint)
        listener.onStart()
    }

    interface Listener {
        fun onStart()
        fun onDrop()
    }
}