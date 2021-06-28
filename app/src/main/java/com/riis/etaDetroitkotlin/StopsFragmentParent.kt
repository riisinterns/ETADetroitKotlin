package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.riis.etaDetroitkotlin.model.RouteStopInfo


/*The StopsFragmentParent is a container fragment that holds the StopsFragmentChild fragment. It is able to switch between different
  ... screens shown by the child fragment corresponding to the following categories: Weekday, Saturday, and Sunday.

  The user can switch between the screens by either swiping across the screen using a viewPager or
  ... by pressing different tabs using a tab layout.
*/

class StopsFragmentParent : Fragment() {

    //CLASS VARIABLES
    //---------------
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

        //Retrieving an updated list of all of the RouteStopInfo objects that are associated with the currently selected route.
        //Each RouteStopInfo object holds information describing a bus stop including its name, location, days of operation, and direction
        sharedViewModel.routeStopsInfoListLiveData.observe(
            viewLifecycleOwner,
            { routeStopsInfo ->
                updateUI(routeStopsInfo) //update the UI using the observed list of RouteStopInfo objects
            })

    }



    //FUNCTION TO UPDATE UI
    //---------------------
    private fun updateUI(routeStops: List<RouteStopInfo>) {

        //set tab menu color
        appBarLayout.setBackgroundColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor))

        //If the currently selected route has bus stops (list of RouteStopInfo objects is not empty):
        if (!routeStops.isNullOrEmpty()) {

            /*NOTE: Since the currently selected route may not have all of the days of operation or directions that are available
              ... in the database, we need to filter through the provided list of RouteStopInfo objects and create lists of all the
              ... days, dayIds, and directionIds that are actually possible for this route.
             */
            val daysLabel = routeStops.map { it.day }
                .distinct() //gets a list of unique (remove repetitions) names
            val daysNumeric =
                routeStops.map { it.dayId }.distinct() // //gets a list of unique dayIds
            val directions: List<Int> =
                routeStops.map { it.directionId }.distinct() //gets a list of unique directionIds
            val dirArg: ArrayList<Int> =
                ArrayList(directions) //converting the directions list to an ArrayList
            val status = 1
            // setting up the tab layout and viewPager
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE) //sets the color of the thin bar that indicates which tab is selected
            stopViewPageAdapter = StopViewPageAdapter(
                this,
                daysNumeric,
                dirArg,
                status
            ) //creating an new instance of StopViewPageAdapter and initializing it
            //... with all the possible dayIds and directionIds belonging to the currently selected route
            viewPager.adapter = stopViewPageAdapter //setting the ViewPager adapter

            //Creating a mediator that links the selected tab in the tab layout with the position of its associated child fragments in the ViewPager.
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                //setting the text of the tab associated with the current ViewPager position with the string
                // ... in the daysLabel list whose index position matches the ViewPager position
                tab.text =
                    daysLabel[position].uppercase() //tab text will appear as uppercase
            }.attach()

        } else { //If the currently selected route does not have any bus stops:

            //the list of dayIds and directionIds both simply hold a single integer value of zero
            val days: List<Int> = listOf(0)
            val dirArg: ArrayList<Int> = ArrayList(listOf(0))
            val status = -1

            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor)) //essentially hiding the SelectedTabIndicator
            stopViewPageAdapter = StopViewPageAdapter(
                this,
                days,
                dirArg,
                status
            ) //creating a new instance of StopViewPageAdapter and initializing it
            //...with the list of dayIds and list of directionIds
            viewPager.adapter = stopViewPageAdapter //setting the ViewPager adapter

            //Link the TabLayout and the ViewPager together.
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        }
    }

    //Class for creating the ViewPager adapter
    private inner class StopViewPageAdapter(
        //takes in a Fragment object, list of dayIds, and a list of directionIds
        fragment: Fragment,
        private var days: List<Int>,
        private var directions: ArrayList<Int>,
        private var status: Int
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = days.size

        //creates a new StopsFragmentChild fragment using a single dayId (selected day of operation) and all of the directionIds for the current route
        //The StopViewPageAdapter will populate the ViewPager with child fragments, each with its own position.
        override fun createFragment(position: Int): Fragment {
            return StopsFragmentChild.newInstance(days[position], directions, status)
        }

    }
}