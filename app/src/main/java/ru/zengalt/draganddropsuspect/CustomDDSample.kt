package ru.zengalt.draganddropsuspect

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.zengalt.draganddropsuspect.dd.DragAndDrop
import ru.zengalt.draganddropsuspect.dd.magnetic.MagneticBehavior

class CustomDDSample : AppCompatActivity() {

    private val paper by lazy { findViewById<ImageView>(R.id.paper) }
    private val trash by lazy { findViewById<ImageView>(R.id.trash) }
    private lateinit var dragAndDrop: DragAndDrop

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paper.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val behavior = MagneticBehavior(
                    500,
                    MagneticBehavior.OnDropToTargetBehavior.StayOnTarget,
                    0.1f,
                    500
                )
                dragAndDrop = DragAndDrop(
                    behavior,
                    paper,
                    trash,
                    Point(event.rawX.toInt(), event.rawY.toInt()),
                    listener = object : DragAndDrop.Listener {
                        override fun onStart() {
                        }

                        override fun onDrop() {
                        }
                    }
                )
            }
            false
        }
    }
}