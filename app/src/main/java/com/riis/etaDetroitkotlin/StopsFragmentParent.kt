package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.riis.etaDetroitkotlin.model.DaysOfOperation
import com.riis.etaDetroitkotlin.model.RouteStopInfo
import com.riis.etaDetroitkotlin.model.RouteStops
import org.w3c.dom.Text

/*The StopsFragmentParent is a container fragment that holds the StopsFragmentChild fragment. It is able to switch between different
  ... screens shown by the child fragment corresponding to the following categories: Weekday, Saturday, and Sunday.

  The user can switch between the screens by either swiping across the screen using a viewPager or
  ... by pressing different tabs using a tab layout.
*/

class StopsFragmentParent : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var days: List<DaysOfOperation>
    private lateinit var tabLayout: TabLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var stopViewPageAdapter: StopViewPageAdapter
    private lateinit var viewPager: ViewPager2

    //links the fragment to a viewModel shared with MainActivity and other fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflating the fragment_stops_parent layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_stops_parent, container, false)

        //referencing layout views using their resource ids
        tabLayout = view.findViewById(R.id.tab_layout)
        appBarLayout = view.findViewById(R.id.app_bar_layout)

        return view
    }

    //UPDATING THE UI BASED ON NEW MODEL DATA
    //---------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager)

        sharedViewModel.routeStopsInfoListLiveData.observe( //we only observe routestops until days has been initialized
            viewLifecycleOwner,
            { routeStopsInfo ->
                updateUI(routeStopsInfo)
            })


    }

    private fun updateUI(routeStops: List<RouteStopInfo>) {

        //set tab menu color
        appBarLayout.setBackgroundColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor))


        if (routeStops.isNotEmpty()) {
            //get all possible values of directions and days
            val daysLabel = routeStops.map { it.day }.distinct()
            val daysNumeric = routeStops.map { it.dayId }.distinct()
            val directions: List<Int> = routeStops.map { it.directionId }.distinct()
            val dirArg: ArrayList<Int> = ArrayList(directions)

            // populate the swipeable fragments
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE)
            stopViewPageAdapter = StopViewPageAdapter(this, daysNumeric, dirArg)
            viewPager.adapter = stopViewPageAdapter

            //puts them in tabs and sets text of tab to the day of operation it filters by
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text =
                    daysLabel[position].uppercase() //access the string corresponding to day id
            }.attach()

        } else { //when route

            val days: List<Int> = listOf(0)
            val dirArg: ArrayList<Int> = ArrayList(listOf(0))

            // populate the swipeable fragments
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor))
            stopViewPageAdapter = StopViewPageAdapter(this, days, dirArg)
            viewPager.adapter = stopViewPageAdapter

            //puts them in tabs and sets text of tab to the day of operation it filters by
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        }
    }

    private inner class StopViewPageAdapter(
        fragment: Fragment,
        private var days: List<Int>,
        private var directions: ArrayList<Int>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = days.size

        override fun createFragment(position: Int): Fragment {
            return StopsFragmentChild.newInstance(days[position], directions)
        }

    }
}