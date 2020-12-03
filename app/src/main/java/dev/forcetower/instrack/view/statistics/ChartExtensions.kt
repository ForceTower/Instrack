package dev.forcetower.instrack.view.statistics

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import dev.forcetower.instrack.R
import dev.forcetower.toolkit.extensions.resolveColorAttr

fun LineChart.configureStatsDefault() {
    val colorOnSurface = context.resolveColorAttr(R.attr.colorOnSurface)
    val colorPrimary = context.resolveColorAttr(R.attr.colorPrimary)
    val font = ResourcesCompat.getFont(context, R.font.product_sans_regular)
    xAxis.apply {
        granularity = 1f
        position = XAxis.XAxisPosition.BOTTOM
        textColor = colorOnSurface
        description.isEnabled = false
        setDrawGridLines(false)
        setDrawAxisLine(false)
        spaceMin = 0.4f
        spaceMax = 0.4f
        typeface = font
    }
    axisLeft.apply {
        axisMinimum = 0f
        granularity = 1f
        textColor = colorOnSurface
        setDrawGridLines(false)
        setDrawAxisLine(false)
        setDrawZeroLine(false)
        typeface = font
    }
    axisRight.apply {
        isEnabled = false
    }
    legend.isEnabled = false
    setNoDataText(context.getString(R.string.no_chart_data))
    setNoDataTextColor(colorPrimary)
    setNoDataTextTypeface(font)
    setTouchEnabled(false)
}

fun LineDataSet.configureStatsDefault(@ColorInt accentColor: Int, drawable: Drawable?) {
    mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    color = accentColor
    fillColor = accentColor
    fillDrawable = drawable
    setDrawFilled(true)
    setDrawCircles(false)
    setDrawValues(false)
}
