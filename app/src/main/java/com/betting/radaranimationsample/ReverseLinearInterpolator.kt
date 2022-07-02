package app.ticktasker.utils

import android.view.animation.LinearInterpolator

class ReverseLinearInterpolator : LinearInterpolator() {
    override fun getInterpolation(input: Float): Float {
        return 1 - input
    }
}