package com.example.msikayaalimi.controller

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

/**
 * Used to ensure button has default application font
 * Code adapted from online course
 */
class MYAButton(context: Context, attrs: AttributeSet): AppCompatButton(context, attrs) {
    init {
        applyFont()
    }

    private fun applyFont() {

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}