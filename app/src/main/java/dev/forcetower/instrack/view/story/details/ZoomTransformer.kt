package dev.forcetower.instrack.view.story.details

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class ZoomTransformer(
    private val max: Float = 1f,
    private val min: Float = 0.8f
) : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pos = (position - 0.5f).coerceIn(-1f..1f)

        // -0.1 -> 0.9
        // +0.1 -> 0.9
        // -0.9 -> 0.1
        val tempScale = 1 - abs(pos)

        // 0.2
        val slope = max - min
        // 0.8 + 0.9 * 0.2
        val scale = min + tempScale * slope

        page.scaleX = scale
        page.scaleY = scale
    }
}
