package com.example.msikayaalimi.controller

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Class to assign default font to textview
 * adapted from online course
 */
class MYATextView (context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs){
    init {
        applyFont()
    }

    private fun applyFont() {

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}