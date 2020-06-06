package dev.forcetower.instrack.view

import android.os.Bundle
import dev.forcetower.instrack.R
import dev.forcetower.toolkit.components.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}