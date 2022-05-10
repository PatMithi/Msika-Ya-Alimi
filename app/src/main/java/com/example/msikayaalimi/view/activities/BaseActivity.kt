package com.example.msikayaalimi.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.msikayaalimi.R
import com.example.msikayaalimi.controller.MYATextView
import com.google.android.material.snackbar.Snackbar

/**
 * Class to store functions which will be used by most classes
 * Code adapted from online courses
 */
open class BaseActivity : AppCompatActivity() {

    private var exitClickedOnce = false

    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun displayProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /* Set the screen content from  a layout resource.
        The resource will be inflated, adding all top-level vies to the screen
         */
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<MYATextView>(R.id.tv_progress_text).text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun dismissProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleClickToExit() {
        if (exitClickedOnce) {
            super.onBackPressed()
            return
        }

        this.exitClickedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.double_click_toExit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({exitClickedOnce = false}, 2000)
    }
}