package com.betting.radaranimationsample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sin

/**
 * @author abhinav chouhan
 * @since 5-jun-2021
 */
class RadarAnimationView(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {
    var rotationAngle = 0f // start angle of the pie
    var pulseRadius = 0f // radius of the pulse animation
    var midPoint = 0f // mid point of the view

    val path = Path() // path of the pie

    //paint to draw the pie and pulse
    private val alphaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(65, 80, 200, 120)
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#50c878")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        midPoint = width / 2f
        path.addArc(0f,
            0f,
            width.toFloat(),
            height.toFloat(), -90f, 90f)
        path.lineTo(width / 2f, width / 2f)
        var degrees = 0L
        CoroutineScope(context = Dispatchers.Default).launch {
            while (true) {
                delay(10)
                ++degrees
                ++rotationAngle
                val angleRadians = Math.toRadians(degrees.toDouble())
                val sine = abs(sin(angleRadians)).toFloat()
                pulseRadius = width / 2f * sine
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        with(canvas) {
            rotate(rotationAngle, midPoint, midPoint)
            drawPath(path, alphaPaint)
            drawLine(
                midPoint,
                midPoint,
                width.toFloat(),
                midPoint,
                linePaint)
            drawLine(midPoint,
                midPoint,
                midPoint,
                0f,
                linePaint)
          drawCircle(midPoint, midPoint, pulseRadius, alphaPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}