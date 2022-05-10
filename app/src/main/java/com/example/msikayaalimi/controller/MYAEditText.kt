package com.example.msikayaalimi.controller

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Code to maintain default application font in the edit text inputs
 * Code adapted from online course
 */
class MYAEditText(context: Context, attrs:AttributeSet): AppCompatEditText(context, attrs) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }

}