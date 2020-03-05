package ru.zengalt.draganddropsuspect.dd.magnetic

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import ru.zengalt.draganddropsuspect.dd.Behavior
import ru.zengalt.draganddropsuspect.dd.magnetic.MagneticBehavior.OnDropToTargetBehavior.*

class MagneticBehavior(
    private val magnetDistance: Int,
    private val dropToTargetBehavior: OnDropToTargetBehavior,
    private val targetMoveWindow: Float,
    private val animationDurationInMillis: Long
) : Behavior() {

    private var isUnderMagnetEffect = false
    private lateinit var animatedDraggingView: AnimatedView
    private lateinit var animatedTargetView: AnimatedView
    private lateinit var touchPoint: Point

    override fun setViews(draggingView: View, targetView: View) {
        animatedDraggingView = AnimatedView(draggingView, animationDurationInMillis)
        animatedTargetView = AnimatedView(targetView, animationDurationInMillis)
    }

    override fun onDrag(globalTouchPoint: Point) {
        touchPoint = globalTouchPoint
        val targetCenter = centerPoint(animatedTargetView)
        when {
            hasEnteredMagnetEffect(targetCenter, globalTouchPoint) -> {
                animatedDraggingView.animateToMovingPoint { centerPoint(animatedTargetView) }
                isUnderMagnetEffect = true
            }
            isMovingUnderMagnetEffect(targetCenter, globalTouchPoint) -> {
                animatedDraggingView.moveToPointIfNotAnimated(targetCenter)
            }
            hasExitedMagnetEffect(targetCenter, globalTouchPoint) -> {
                isUnderMagnetEffect = false
                animatedDraggingView.animateToMovingPoint { touchPoint }
            }
            else -> animatedDraggingView.moveToPointIfNotAnimated(globalTouchPoint)
        }

        animatedTargetView.moveCloserToPointIfNotAnimated(globalTouchPoint, targetMoveWindow)
    }

    override fun onStart(globalTouchPoint: Point) {
        touchPoint = globalTouchPoint
        animatedTargetView.animateCloserToMovingPoint(targetMoveWindow) { touchPoint }
        val targetCenter = centerPoint(animatedTargetView)

        if (hasEnteredMagnetEffect(targetCenter, globalTouchPoint)) {
            animatedDraggingView.animateToMovingPoint { centerPoint(animatedTargetView) }
        } else
            animatedDraggingView.animateToMovingPoint { touchPoint }

    }

    override fun onFinish() {
        if (animatedDraggingView.intersect(animatedTargetView)) {
            when (dropToTargetBehavior) {
                Hide -> animatedDraggingView.view.visibility = View.GONE
                Return -> animatedDraggingView.returnToStartingPosition()
                StayOnTarget -> animatedDraggingView.fixToMovingPoint {
                    centerPoint(
                        animatedTargetView
                    )
                }
            }
        } else
            animatedDraggingView.returnToStartingPosition()

        animatedTargetView.returnToStartingPosition()
    }

    private fun distance(center1: Point, center2: Point): Double {
        val dx = (center1.x - center2.x).toDouble()
        val dy = (center1.y - center2.y).toDouble()
        return Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0))
    }

    private fun hasExitedMagnetEffect(targetCenter: Point, globalTouchPoint: Point): Boolean {
        val distance = distance(targetCenter, globalTouchPoint)
        return distance > magnetDistance && isUnderMagnetEffect
    }

    private fun hasEnteredMagnetEffect(targetCenter: Point, globalTouchPoint: Point): Boolean {
        val distance = distance(targetCenter, globalTouchPoint)
        return distance < magnetDistance && !isUnderMagnetEffect
    }

    private fun isMovingUnderMagnetEffect(targetCenter: Point, globalTouchPoint: Point): Boolean {
        val distance = distance(targetCenter, globalTouchPoint)
        return distance < magnetDistance && isUnderMagnetEffect
    }

    private fun centerPoint(animatedView: AnimatedView): Point {
        val visibleRect = Rect()
        animatedView.view.getGlobalVisibleRect(visibleRect)
        return Point(visibleRect.centerX(), visibleRect.centerY())
    }

    sealed class OnDropToTargetBehavior {
        object Return : OnDropToTargetBehavior()
        object StayOnTarget : OnDropToTargetBehavior()
        object Hide : OnDropToTargetBehavior()
    }
}