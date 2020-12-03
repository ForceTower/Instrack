package dev.forcetower.instrack.view.home

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter

@BindingAdapter("imageTint")
fun ImageView.imageTint(@ColorInt color: Int) {
    this.imageTintList = ColorStateList.valueOf(color)
}
