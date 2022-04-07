package com.example.msikayaalimi.ui.fragments

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.ui.activities.MyCartActivity
import com.example.msikayaalimi.ui.activities.SettingsActivity
import com.example.msikayaalimi.ui.adapters.MarketItemsAdapter
import com.example.msikayaalimi.ui.adapters.UserProductsListAdapter
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYATextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : BaseFragment() {

  private lateinit var binding:View
  private lateinit var mCategory:String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    /*
     If we want to use the option menu
     in fragment we need to add it.
     */
    setHasOptionsMenu(true)

  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.dashboard_menu_top, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId

    when (id) {

      R.id.app_settings -> {

        startActivity(Intent(activity, SettingsActivity::class.java))
        return true
      }

      R.id.my_cart -> {

        startActivity(Intent(activity, MyCartActivity::class.java))
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
    binding = root

    return root
  }

  override fun onResume() {
    super.onResume()

    GlobalScope.launch(Dispatchers.Main) {
      getMarketItems()
    }
  }

  suspend fun marketItemsSuccessfullyLoaded(marketItemsList:ArrayList<Product>) {
    hideProgressDialog()

    if (marketItemsList.size > 0) {

      binding.findViewById<RecyclerView>(R.id.market_items_rv).visibility = View.VISIBLE
      binding.findViewById<MYATextView>(R.id.tv_no_dashboard_items_found).visibility = View.GONE

      binding.findViewById<RecyclerView>(R.id.market_items_rv).layoutManager = GridLayoutManager(activity, 2)
      binding.findViewById<RecyclerView>(R.id.market_items_rv).setHasFixedSize(true)

      val productsAdapter = MarketItemsAdapter(requireActivity(),marketItemsList)
      binding.findViewById<RecyclerView>(R.id.market_items_rv).adapter = productsAdapter
    } else {
      binding.findViewById<RecyclerView>(R.id.market_items_rv).visibility = View.GONE
      binding.findViewById<MYATextView>(R.id.tv_no_dashboard_items_found).visibility = View.VISIBLE
    }
  }

  private suspend fun getMarketItems() {
    // show progress dialog
    showProgressDialog(resources.getString(R.string.please_wait))

    FirestoreClass().getMarketItems(this@DashboardFragment)
  }



}