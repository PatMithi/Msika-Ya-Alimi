package com.example.msikayaalimi.controller

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * Class to maintain app font in radio button
 * adapted from online
 */
class MYARadioButton(context: Context, attributeSet: AttributeSet):AppCompatRadioButton(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}