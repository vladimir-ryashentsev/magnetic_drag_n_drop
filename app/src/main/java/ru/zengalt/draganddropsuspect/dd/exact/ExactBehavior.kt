package ru.zengalt.draganddropsuspect.dd.exact

import android.graphics.Point
import android.view.View
import ru.zengalt.draganddropsuspect.dd.Behavior
import ru.zengalt.draganddropsuspect.dd.magnetic.AnimatedView

class ExactBehavior : Behavior() {

    lateinit var animatedView: AnimatedView

    override fun setViews(draggingView: View, targetView: View) {
        animatedView = AnimatedView(draggingView, 0)
    }

    override fun onDrag(globalTouchPoint: Point) {
        animatedView.moveToPoint(globalTouchPoint)
    }

    override fun onStart(globalTouchPoint: Point) = Unit

    override fun onFinish() = Unit

}