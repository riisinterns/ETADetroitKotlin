package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.riis.etaDetroitkotlin.model.DaysOfOperation
import com.riis.etaDetroitkotlin.model.RouteStops

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

        //Getting an updated list of DaysOperation objects (only 4 items: weekday, everyday, saturday, sunday)
        sharedViewModel.daysOfOperationLiveData.observe(viewLifecycleOwner, {
            days = it

            //Getting an updated list of RouteStops objects (these are the stops for the currently selected route saved in the SharedViewModel).
            // This is implemented as a nested observer so that the RouteStops objects are only observed after the days variable has been initialized
            sharedViewModel.routeStopsListLiveData.observe(
                viewLifecycleOwner,
                { routeStops ->
                    updateUI(routeStops) //update the UI using the list of RouteStops
                })
        })

    }
        //function to update fragment's UI
        private fun updateUI(routeStops: List<RouteStops>) {

        //set tab menu color
        appBarLayout.setBackgroundColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor))

        //If the list of route stops is not empty:
        if (routeStops.isNotEmpty()) {

            //Since the selected route may not have all the days in the list of DaysOfOperation objects, we need a another list with only the days
            // ... the selected route does have. To do this we filter through the routeStops list and get a list of unique (remove repetitions) dayIds
            val days = routeStops.map { it.dayId }.distinct()

            val directions: List<Int> = routeStops.map { it.directionId }.distinct() //gets a list of unique (remove repetitions) directionIds
            val dirArg: ArrayList<Int> = ArrayList(directions) //converting the directions list into an ArrayList

            // setting up the tab layout and viewPager
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE) //sets the color of the bar that indicates which tab is selected
            stopViewPageAdapter = StopViewPageAdapter(this, days, dirArg) //creating an new instance of StopViewPageAdapter and initializing it
                                                                                  //... with all the possible days and directions belonging to the selected route
            viewPager.adapter = stopViewPageAdapter //setting the ViewPager adapter

            //Creating a mediator that links the selected tab in the tab layout with the position of child fragments in the ViewPager.
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                //Getting the DaysOfOperation object (from this.days) whose id matches the dayId in the days list, using the ViewPager's current position as the index
                val day =
                    this.days.filter { it.id == days[position] } // since all id's are unique, this will return singleton (list with only one element)

                tab.text = day[0].day.uppercase() //setting the text of the tab associated with the current ViewPager position with the string
                                                  // ... belonging to the DaysOfOperation object stored in the day variable above
            }.attach()

        }else{ //If the list of route stops is empty:

            //the list of days and directions both simply hold a integer of value zero
            val days: List<Int> = listOf(0)
            val dirArg: ArrayList<Int> = ArrayList(listOf(0))

            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sharedViewModel.currentCompany?.brandColor)) //essentially hiding the SelectedTabIndicator
            stopViewPageAdapter = StopViewPageAdapter(this, days, dirArg) //creating an new instance of StopViewPageAdapter and initializing it
                                                                                  //... the list of days and list of directions
            viewPager.adapter = stopViewPageAdapter

            //Creating a mediator that links the selected tab in the tab layout with the position of child fragments in the ViewPager.
            TabLayoutMediator(tabLayout, viewPager){_,_->}.attach() //body is empty because we don't want do anything with tabs here
        }
    }

    //Class for creating the ViewPager adapter
    private inner class StopViewPageAdapter(
        //takes in a Fragment object, list of dayIds for the selected route, and a list of directionIds from the selected route
        fragment: Fragment,
        private var days: List<Int>,
        private var directions: ArrayList<Int>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = days.size //size of list helps adapter automatically create positions for its child fragments

        //creates a new StopsFragmentChild fragment for each dayId in the provided days list. It also gives each one the list of directionIds for the selected route
        override fun createFragment(position: Int): Fragment {
            return StopsFragmentChild.newInstance(days[position], directions)
        }
        //The StopViewPageAdapter will populate the ViewPager with child fragments, each with its own position.
    }
}