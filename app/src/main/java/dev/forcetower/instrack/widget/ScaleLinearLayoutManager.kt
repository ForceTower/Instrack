package dev.forcetower.instrack.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.abs

class ScaleLinearLayoutManager @JvmOverloads constructor(
    context: Context,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    private val mShrinkAmount = 0.12f
    private val mShrinkDistance = 0.3f

    @Keep
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : this(context) {
        val properties = getProperties(context, attrs, defStyleAttr, defStyleRes)
        orientation = properties.orientation
        reverseLayout = properties.reverseLayout
        stackFromEnd = properties.stackFromEnd
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            middle()
            scrolled
        } else {
            0
        }
    }

    override fun scrollToPosition(position: Int) {
        super.scrollToPosition(position)
        getChildAt(0)?.postDelayed({
            middle()
        }, 50)
    }

    fun middle() {
        val midpoint = width / 2f
        val d0 = 0f
        val d1: Float = mShrinkDistance * midpoint
        val s0 = 1f
        val s1: Float = 1f - mShrinkAmount
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)!!
            val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f

            val d = d1.coerceAtMost(abs(midpoint - childMidpoint))
            val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
            child.scaleX = scale
            child.scaleY = scale
        }
    }
}