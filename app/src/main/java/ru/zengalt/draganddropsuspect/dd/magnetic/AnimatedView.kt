package ru.zengalt.draganddropsuspect.dd.magnetic

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class AnimatedView(
    val view: View,
    private val animationDurationInMillis: Long
) {

    private var animator: Animator? = null

    fun intersect(anotherAnimatedView: AnimatedView): Boolean {
        val rect1 = Rect()
        view.getGlobalVisibleRect(rect1)
        val rect2 = Rect()
        anotherAnimatedView.view.getGlobalVisibleRect(rect2)
        return rect1.intersect(rect2)
    }

    fun animateCloserToMovingPoint(moveWindow: Float, pointSource: () -> Point) {
        val fromX = view.translationX
        val fromY = view.translationY

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = animationDurationInMillis
            addUpdateListener {
                val center = centerPoint(view)
                val touch = pointSource.invoke()

                val dx = touch.x - center.x
                val dy = touch.y - center.y

                val toX = dx * moveWindow
                val toY = dy * moveWindow

                val stepX = toX - (toX - fromX) * (1 - it.animatedValue as Float)
                val stepY = toY - (toY - fromY) * (1 - it.animatedValue as Float)

                view.translationX = stepX
                view.translationY = stepY
            }
            start()
        }
    }

    fun moveCloserToPointIfNotAnimated(globalTouchPoint: Point, moveWindow: Float) {
        if (animator?.isRunning == true)
            return

        val center = centerPoint(view)
        val dx = globalTouchPoint.x - center.x
        val dy = globalTouchPoint.y - center.y

        view.translationX = dx * moveWindow
        view.translationY = dy * moveWindow
    }

    fun returnToStartingPosition() {
        val x = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0F)
        val y = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0F)
        animator = ObjectAnimator.ofPropertyValuesHolder(view, x, y).apply {
            duration = animationDurationInMillis
            interpolator = FastOutSlowInInterpolator()
            start()
        }
    }

    fun fixToMovingPoint(pointSource: () -> Point) {
        animator?.cancel()

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = animationDurationInMillis
            addUpdateListener {
                moveToPoint(pointSource.invoke())
            }
            start()
        }
    }

    fun animateToMovingPoint(pointSource: () -> Point) {
        animator?.cancel()

        val fromX = view.translationX
        val fromY = view.translationY

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            interpolator = FastOutSlowInInterpolator()
            duration = animationDurationInMillis
            addUpdateListener {
                val viewCenter = centerPoint(view)
                val toX = pointSource.invoke().x - viewCenter.x + view.translationX
                val toY = pointSource.invoke().y - viewCenter.y + view.translationY

                val stepX = toX - (toX - fromX) * (1 - it.animatedValue as Float)
                val stepY = toY - (toY - fromY) * (1 - it.animatedValue as Float)

                view.translationX = stepX
                view.translationY = stepY
            }
            start()
        }
    }

    fun moveToPointIfNotAnimated(point: Point) {
        if (animator?.isRunning == true)
            return

        moveToPoint(point)
    }

    fun moveToPoint(point: Point) {
        val center = centerPoint(view)

        val dx = point.x - center.x
        val dy = point.y - center.y

        view.translationX += dx.toFloat()
        view.translationY += dy.toFloat()
    }

    private fun centerPoint(view: View): Point {
        val visibleRect = Rect()
        view.getGlobalVisibleRect(visibleRect)
        return Point(visibleRect.centerX(), visibleRect.centerY())
    }
}