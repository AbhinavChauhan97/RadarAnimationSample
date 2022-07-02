package com.abhinav.chouhan.openglsample

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import com.betting.radaranimationsample.R
import kotlin.math.atan2
import kotlin.random.Random

class FlyingPlanes(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private val planePath = Path()

    private val random = Random(System.currentTimeMillis())
    private var planes:Array<ImageView>
    private var pathMeasures = Array(3){PathMeasure()}


    private val planePathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
    }


    init {
        setWillNotDraw(false)

        planes = Array(3) { i ->
            val plane = ImageView(context).apply {
                layoutParams = LayoutParams(100, 80)
                setImageResource(
                    when (i) {
                        0 -> R.drawable.jet1
                        1 -> R.drawable.jet2
                        else -> R.drawable.jet3
                    }
                )
            }
            addView(plane)
            plane
        }

    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initPlanePath()
        pathMeasures.forEach { pathMeasure ->
            pathMeasure.setPath(planePath,false)
        }
        for(i in 0..2){
            val plane = planes[i]

            val pathMeasure = pathMeasures[i]
            flyPlane(plane,pathMeasure)
        }
    }


    private fun initPlanePath() {
        planePath.reset()
        var startX = randomX()
        var startY = randomY()
        repeat(4) {
            planePath.moveTo(startX, startY)
            val controlX = randomX()
            val controlY = randomY()
            val endX = randomX()
            val endY = randomY()
            planePath.quadTo(controlX, controlY, endX, endY)
            startX = endX
            startY = endY
        }
    }

    private fun flyPlane(plane: ImageView, pathMeasure: PathMeasure,reverse:Boolean = false) {
        var distance = 0f
        val tan = floatArrayOf(0f,0f)
        val pos = floatArrayOf(0f, 0f)
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = (pathMeasure.length * random.nextInt(3,8)).toLong()
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            distance = it.animatedValue as Float
            pathMeasure.getPosTan(if(reverse) 1 - distance else distance* pathMeasure.length, pos, tan)
            val planeX = pos[0]
            val planeY = pos[1]
            plane.x = planeX
            plane.y = planeY
            val degrees = atan2(tan[1], tan[0]) * 180.0 / Math.PI
            plane.rotation = degrees.toFloat() - 180
        }
        valueAnimator.doOnEnd {
            if (pathMeasure.nextContour()) {
                flyPlane(plane, pathMeasure)
            }else{
                initPlanePath()
                pathMeasure.setPath(planePath,false)
                flyPlane(plane,pathMeasure,true)
            }
        }

        valueAnimator.start()
    }

    private fun randomX() = random.nextInt(0, width - 100).toFloat()
    private fun randomY() = random.nextInt(0, height - 100).toFloat()

}

