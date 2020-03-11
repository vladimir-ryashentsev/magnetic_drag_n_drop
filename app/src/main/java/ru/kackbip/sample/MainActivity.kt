package ru.kackbip.sample

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.kackbip.magneticdragndrop.ActionAfterDropInside
import ru.kackbip.magneticdragndrop.MagneticDragAndDrop
import ru.zengalt.draganddropsuspect.R

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
                    .listener(object : MagneticDragAndDrop.Listener {
                        override fun onStart() {
                        }

                        override fun onDropOuside() {
                        }

                        override fun onDropInside() = ActionAfterDropInside.KEEP_ON_TARGET
                    })
                    .build()
            }
            false
        }
    }
}