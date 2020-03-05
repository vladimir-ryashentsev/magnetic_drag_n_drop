package ru.zengalt.draganddropsuspect.dd

import android.graphics.Point
import android.view.View

abstract class Behavior {

    abstract fun setViews(draggingView: View, targetView: View)

    abstract fun onDrag(globalTouchPoint: Point)
    abstract fun onStart(globalTouchPoint: Point)
    abstract fun onFinish()
}