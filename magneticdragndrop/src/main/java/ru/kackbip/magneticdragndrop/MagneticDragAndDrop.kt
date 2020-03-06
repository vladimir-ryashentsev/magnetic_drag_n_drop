package ru.kackbip.magneticdragndrop

import android.graphics.Point
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View

class MagneticDragAndDrop private constructor(
    private val viewsMover: ru.kackbip.magneticdragndrop.ViewsMover,
    draggingView: View,
    targetView: View,
    startingGlobalPoint: Point,
    private val listener: Listener? = null
) {

    init {
        viewsMover.setViews(draggingView, targetView)

        val root = draggingView.rootView
        root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                val touchPoint = Point(event.rawX.toInt(), event.rawY.toInt())
                viewsMover.onDrag(touchPoint)
            } else if (event.action == MotionEvent.ACTION_UP) {
                root.setOnTouchListener(null)
                if (viewsMover.onDrop())
                    listener?.onDrop()
            }
            true
        }

        viewsMover.onStart(startingGlobalPoint)
        listener?.onStart()
    }

    interface Listener {
        fun onStart()
        fun onDrop()
    }

    class Builder(private val draggingView: View, private val targetView: View) {
        private var startingGlobalPoint = Point().apply {
            val visibleRect = Rect()
            draggingView.getGlobalVisibleRect(visibleRect)
            x = visibleRect.centerX()
            y = visibleRect.centerY()
        }
        private var listener: Listener? = null
        private var magnetDistance: Int = 300
        private var targetMoveWindow: Float = 0.1f
        private var animationDurationInMillis: Long = 500

        fun startingGlobalPoint(point: Point): Builder {
            startingGlobalPoint = point
            return this
        }

        fun listener(listener: Listener): Builder {
            this.listener = listener
            return this
        }

        fun magnetDistance(magnetDistance: Int): Builder {
            this.magnetDistance = magnetDistance
            return this
        }

        fun targetMoveWindow(targetMoveWindow: Float): Builder {
            this.targetMoveWindow = targetMoveWindow
            return this
        }

        fun animationDurationInMillis(animationDurationInMillis: Long): Builder {
            this.animationDurationInMillis = animationDurationInMillis
            return this
        }

        fun build(): MagneticDragAndDrop {
            val viewsMover =
                ru.kackbip.magneticdragndrop.ViewsMover(
                    magnetDistance,
                    targetMoveWindow,
                    animationDurationInMillis
                )
            return MagneticDragAndDrop(
                viewsMover,
                draggingView,
                targetView,
                startingGlobalPoint,
                listener
            )
        }

    }
}