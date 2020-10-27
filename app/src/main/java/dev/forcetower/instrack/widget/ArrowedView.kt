package dev.forcetower.instrack.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import dev.forcetower.instrack.R

class ArrowedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private val path = Path()

    var color = Color.WHITE

    init {
        init(attrs, defStyleAttr, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs ?: return

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ArrowedView,
            defStyleAttr, defStyleRes
        )

        try {
            color = typedArray.getInt(R.styleable.ArrowedView_color, color)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        path.fillType = Path.FillType.EVEN_ODD

        val length = measuredWidth.toFloat()
        val pointerSize = 140f
        val halfPointerSize = pointerSize / 2
        val sideLength = (length - pointerSize) / 2

        path.reset()
        path.moveTo(sideLength, 60f)
        path.lineTo(sideLength + halfPointerSize - 8, 3f)
        path.addArc(
            sideLength + halfPointerSize - 10, 0f,
            sideLength + halfPointerSize + 10, 10f, 210f, 120f
        )
        path.lineTo(sideLength + halfPointerSize * 2, 60f)
        path.lineTo(sideLength, 60f)
        path.close()

        paint.color = color
        canvas.drawPath(path, paint)
    }
}