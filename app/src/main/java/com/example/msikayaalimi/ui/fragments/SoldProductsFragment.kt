package com.example.msikayaalimi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.SoldProduct

private lateinit var binding:View

/**
 * A simple [Fragment] subclass.
 * Use the [SoldProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldProductsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_sold_products, container, false)
        binding = root
        return root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getSoldProductsList(){

    }

    fun successfullyLoadedSoldProducts(soldProduct: ArrayList<SoldProduct>){

        hideProgressDialog()



    }

}