package dev.forcetower.instrack.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(context: Context, @DimenRes itemOffsetId: Int) : RecyclerView.ItemDecoration() {
    private val mItemOffset: Int = context.resources.getDimensionPixelSize(itemOffsetId)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        if (position != 0) {
            if (position % 2 == 0) {
                outRect.set(0, 0, mItemOffset, 0)
            } else {
                outRect.set(mItemOffset, 0, 0, 0)
            }
        }
    }
}
