package com.example.msikayaalimi.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.view.activities.MyCartActivity
import com.example.msikayaalimi.view.activities.SettingsActivity
import com.example.msikayaalimi.view.adapters.MarketItemsAdapter
import com.example.msikayaalimi.controller.MYATextView
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Fragment used to load the items from Google Firebase and load them into the fragment components
 * code adaplted from online course
 */
class MarketFragment : BaseFragment() {

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

    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_market).stopShimmerAnimation()
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_market).visibility = View.GONE

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
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_market).startShimmerAnimation()

    FirestoreClass().getMarketItems(this@MarketFragment)
  }



}