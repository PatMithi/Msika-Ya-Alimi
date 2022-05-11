package com.example.msikayaalimi.view.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.msikayaalimi.R
import com.example.msikayaalimi.controller.MYATextView

/**
 * Fragment used to store functions which will be used by multiple fragments
 * code adapted from online course
 */
open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun displayProgressDialog(text:String) {

        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<MYATextView>(R.id.tv_progress_text).text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun dismissProgressDialog() {
        mProgressDialog.dismiss()
    }
}