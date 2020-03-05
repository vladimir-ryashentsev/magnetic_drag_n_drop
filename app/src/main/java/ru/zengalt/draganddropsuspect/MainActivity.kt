package ru.zengalt.draganddropsuspect

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.zengalt.draganddropsuspect.dd.MagneticDragAndDrop

class MainActivity : AppCompatActivity() {

    private val paper by lazy { findViewById<ImageView>(R.id.paper) }
    private val trash by lazy { findViewById<ImageView>(R.id.trash) }
    private lateinit var magneticDragAndDrop: MagneticDragAndDrop

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO make long click and drag-n-drop friends

        paper.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val startingPoint = Point(event.rawX.toInt(), event.rawY.toInt())
                magneticDragAndDrop = MagneticDragAndDrop.Builder(paper, trash)
                    .startingGlobalPoint(startingPoint)
                    .build()
            }
            false
        }
    }
}