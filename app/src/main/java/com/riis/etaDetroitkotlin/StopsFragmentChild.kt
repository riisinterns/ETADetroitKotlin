package com.riis.etaDetroitkotlin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Routes
import com.riis.etaDetroitkotlin.model.Stops
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.riis.etaDetroitkotlin.model.RouteStopInfo
import java.util.*

private const val TAG = "StopsFragment"
private const val DAY_KEY = "day_key"
private const val DIRECTIONS_KEY = "directions_key"

//This fragment is a child fragment that will be displayed within the StopsFragmentParent's ViewPager.
//It contains a recycler view that displays a list of bus stops for the currently selected route and their associated timings

class StopsFragmentChild : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var searchView: SearchView
    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private lateinit var directionFab: FloatingActionButton
    private lateinit var noRoutesTextView: TextView
    private var stopsVisibility: HashMap<Int, Int> = hashMapOf()
    private var tripStopsPositions: HashMap<Int, Int> = hashMapOf()
    private var day: Int? = 0
    private var directions: List<Int>? = mutableListOf()
    private lateinit var routeStopsInfo: List<RouteStopInfo>

    //links the fragment to a viewModel shared with MainActivity and other fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //CREATING AND INITIALIZING THE FRAGMENT
    //--------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //retrieving the arguments from the bundle associated with the keys DAY_KEY and DIRECTIONS_KEY
        day = arguments?.getInt(DAY_KEY)
        directions = arguments?.getIntegerArrayList(DIRECTIONS_KEY)

        //sharedViewModel.direction represents the directionId that dictates the arrow direction of the directionFab FloatingActionButton.
        //It is initially set to zero and acts as the index for the directions list
        sharedViewModel.direction = directions?.get(sharedViewModel.directionCount) ?: 0

        //NOTE: In the case that there are no bus stops for the selected route, the parent fragment passes a value of zero for the dayId
        // ... and a directions list with a single value of zero. This results in day = 0 and sharedViewModel.direction = 0.
    }

    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflating the fragment_stops_child layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_stops_child, container, false)

        //referencing layout views using their resource ids
        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        directionFab = view.findViewById(R.id.fab)
        noRoutesTextView = view.findViewById(R.id.NoRouteLbl)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        setDirectionImage() //update directionFab UI
        setUpAppBar() //setup the AppBar UI
        setHasOptionsMenu(true) //allows this fragment to be able to add its own menu options to the Main Activity's app bar

        return view
    }

    //UPDATING THE UI BASED ON NEW MODEL DATA
    //---------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if there exists at least one bus stop for the selected route:
        if (day != 0 && sharedViewModel.direction != 0) {

            //Retrieving an updated list of all of the RouteStopInfo objects that are associated with the currently selected route.
            //Each RouteStopInfo object holds information describing a bus stop including its name, location, days of operation, and direction
            sharedViewModel.routeStopsInfoListLiveData.observe(
                viewLifecycleOwner,
                { routeStopsInfo ->
                    this.routeStopsInfo =
                        routeStopsInfo //saving the list of RouteStopInfo objects to a class variable

                    //filter the routeStopsInfo list to only keep RouteStopInfo objects whose directionId and dayId match their respective ids
                    // ... currently saved to the SharedViewModel. Then update the UI using the filtered list.
                    updateUI(routeStopsInfo.filter {
                        it.directionId == sharedViewModel.direction && it.dayId == day
                    })
                }
            )
        } else { //if there are no bus stops for the selected route:

            //Hide the recycler view and floating action button
            stopsRecyclerView.visibility = View.GONE
            directionFab.visibility = View.GONE

            noRoutesTextView.visibility =
                View.VISIBLE //display the text view that informs the user that the current route has no bus stops
        }
    }

    //WHEN THE FRAGMENT BECOMES VISIBLE
    //---------------------------------
    override fun onStart() {
        super.onStart()
        directionFab.setOnClickListener { //When the directionFab floating action button is clicked:
            val listExhausted = sharedViewModel.directionCount + 1 < (directions?.size ?: -1)

            //go to next direction in the directions list if the list hasn't been exhausted. Then save the directionId to the sharedViewModel
            sharedViewModel.direction = if (listExhausted) {
                directions?.get(++sharedViewModel.directionCount) ?: 0
            } else {
                //if the directions list has been exhausted, go back to first direction in the list and save its directionId to the sharedViewModel
                sharedViewModel.directionCount =
                    0 //if list has been exhausted go back to first element
                directions?.get(sharedViewModel.directionCount) ?: 0
            }
            setDirectionImage() //update the directionFab floating action button UI

            //update the recycler view with the recently saved directionId as a filter
            updateUI(this.routeStopsInfo.filter { it.directionId == sharedViewModel.direction && it.dayId == day })
        }
    }

    //ADDING MENU OPTIONS TO THE APP BAR PROVIDED BY MAIN ACTIVITY
    //------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            R.menu.search_menu,
            menu
        ) //search_menu.xml displays a search icon (magnifying glass) in the top right of the app bar

        //creating and configuring the search bar
        val searchIcon = menu.findItem(R.id.search_icon)
        searchView =
            searchIcon?.actionView as SearchView  //SearchView widget implements an action view for entering search queries
        searchView.imeOptions =
            EditorInfo.IME_ACTION_DONE //replaces the user's carriage return button in their on-screen keyboard
        // with a "Done" action button (may appear as a check mark)

        //handling interactions with the search bar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //when user clicks submit button after entering query...
            override fun onQueryTextSubmit(s: String): Boolean {
                return false //return false to let the SearchView handle the submission by launching any associated intent
            }

            //when the query text is changed by the user
            override fun onQueryTextChange(s: String): Boolean {
                adapter.filter.filter(s) //uses the StopAdapter's filter function to asynchronously filter the list items in the recycler view using a search query
                return false
            }
        })

    }

    //Function to set the UI of the directionFab floating action button
    private fun setDirectionImage() {
        //getting the correct arrow image drawable based on the directionId currently saved to the shared view model
        var drawable = when (sharedViewModel.direction) {
            1 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
            2 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up)
            3 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_left)
            4 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_right)
            else -> null
        }

        //If a drawable exist, use it to set the image drawable of directionFab
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable)
            //tinting the arrow portion of the drawable with a white color
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            directionFab.setImageDrawable(drawable)
        } else {
            directionFab.visibility = View.GONE
        }
    }

    //Function to update the recycler view UI when the list of RouteStopInfo objects changes
    private fun updateUI(routeStops: List<RouteStopInfo>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }

    //Function to update the app bar UI
    private fun setUpAppBar() {
        //accessing MainActivity's app bar and setting its title to the name of the currently selected Route
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "${sharedViewModel.currentRoute?.name}"

        //accessing MainActivity's app bar and setting its color to the color of the currently selected Company
        val appBarColor = ColorDrawable(sharedViewModel.currentCompany?.brandColor?.toColorInt()!!) //converts hex string -> int -> ColorDrawable
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)
    }

    //Function that can be accessed by other fragments to create a new instance of this fragment and initialize it with arguments
    companion object {
        fun newInstance(day: Int, directions: ArrayList<Int>): StopsFragmentChild {
            //accepts a dayId and list of directionIds as arguments and saves them to the bundle using specific keys
            val args = Bundle().apply {
                putInt(DAY_KEY, day)
                putIntegerArrayList(DIRECTIONS_KEY, directions)
            }
            //sets the fragment's arguments to the bundle arguments
            return StopsFragmentChild().apply {
                arguments = args
            }
        }
    }

    //VIEW HOLDER CLASS FOR RECYCLER VIEW
    //-----------------------------------
    private inner class StopHolder(view: View) :
    //When given a view from the StopAdapter, it is used as a reusable blueprint for creating itemViews
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var routeStopInfoItem: RouteStopInfo //instantiating a new RouteStopInfo object to receive specific model layer data
                                                              // ... and use that to configure an itemView

        //referencing the itemView's child views from the list_item_stop layout
        private var allArrivalTimes: TextView = view.findViewById(R.id.all_arrival_times)
        private val stopName: TextView = view.findViewById(R.id.stop_name)
        private val currentTime: TextView = view.findViewById(R.id.current_time)
        private val arrivalTimeLabel: TextView = view.findViewById(R.id.arrival_time_label)
        private var dynamicLinearLayout =
            view.findViewById(R.id.dynamic_linear_layout) as LinearLayout

        private var staticConstraintLayout: ConstraintLayout = view.findViewById(R.id.static_constraint_layout)

        //initializing the viewHolder
        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
            dynamicLinearLayout.visibility = View.GONE //each itemView's expandable LinearLayout (for showing bus times) is initially invisible
        }

        //Configuring the attributes of each itemView using a specific RouteStopInfo object from the model layer, chosen by the StopAdapter
        fun bind(routeStopInfo: RouteStopInfo) {
            routeStopInfoItem = routeStopInfo

            stopName.text = routeStopInfoItem.name //setting and displaying each list item's bus stop name
            allArrivalTimes.text = null //The textView displaying the bus arrival times is initially set to be empty

            /*NOTE: When a user clicks on an item view, its dynamicLinearLayout expands to display a list of bus arrival times. However, when the user scrolls
                    ... such that the itemView is no longer on screen, it is destroyed. When the user scrolls to return the itemView to the screen, it is recreated
                    ... with its dynamicLinearLayout collapsed, which is undesirable (UI is not consistent). To prevent this, we must record the position of each
                    ... itemView and whether its dynamicLinearLayout is visible using a hashmap in order to persist itemView UI across death.
             */

            //If the itemView's stopId is not in the stopsVisibility hashmap, add it with value View.GONE (since it has not been seen by the user yet)
            if (routeStopInfoItem.stopId !in stopsVisibility) {
                stopsVisibility[routeStopInfoItem.stopId] = View.GONE
            }
            //set the visibility of the itemView's dynamicLinearLayout to the visibility of its respective stopID in the stopsVisibility hashmap
            dynamicLinearLayout.visibility = stopsVisibility[routeStopInfoItem.stopId]!!

            if (dynamicLinearLayout.visibility == View.VISIBLE) { //if the itemView is currently visible on screen, set and display its bus arrival times
                setArrivalTimes()
            }

            //NOTE: Regardless of whether the itemView's dynamicLinearLayout is visible, it always displays the next bus stop time and how long it will take to occur.

            //Obtaining an updated list of TripStops objects from the database that are associated with the itemView's stopId, and update its UI accordingly.
            // A TripStop object stores an arrival time for a particular bus stop (stopId).
            sharedViewModel.getArrivalTimes(routeStopInfoItem.stopId).observe(
                viewLifecycleOwner,
                { tripStop ->
                    //sorting the list of TripStops objects using their arrivalTime attribute (Date object), from earliest to latest
                    val sortedTripStops = tripStop.sortedBy { it.arrivalTime }

                    //If the itemView's stopId produces a list of TripStops with more than one element (any less can't be considered a valid bus stop):
                    if (sortedTripStops.size > 1) {
                        //iterate through each TripStops object and calculate the time difference between its arrivalTime and the user's current time
                        for (i in sortedTripStops.indices) {
                            val difference: Long =
                                sortedTripStops[i].arrivalTime?.time!! - Date(Calendar.getInstance().timeInMillis).time //result in milliseconds

                            /* While iterating through the sorted list of TripStops, we look for the one that has the smallest positive time difference.
                               This will contain the first available arrival time for the selected bus stop. Once the closest TripStop has been found,
                               ... stop iterating and record its position in the sorted list of TripStops using the tripStopsPositions hashmap.
                             */
                            if (difference > 0) {
                                //convert the time difference calculated above into minutes and seconds
                                val seconds = difference / 1000
                                val minutes = seconds / 60

                                //display the now formatted time difference in the itemView
                                currentTime.text = "(${
                                    sortedTripStops[i].arrivalTime.toString()
                                        .substring(11, 16)
                                })"
                                arrivalTimeLabel.text = "Next Stop: $minutes Minutes"

                                /*NOTE: Each bus stop (itemView stopId) has an associated tripStop that contains the earliest bus arrival time for that stop.
                                        To keep a reference of this, we map the stopId of each bus stop to the index of its earliest tripStop from the sortedTripStops list
                                */
                                tripStopsPositions[routeStopInfoItem.stopId] = i
                                break
                            }

                        }
                    } else { //If the itemView's stopId produces a list of TripStops with one element or less:
                        currentTime.text = ""
                        arrivalTimeLabel.text = "No Stop Times Found"
                    }
                }

            )
        }

        //when an item view is clicked on:
        override fun onClick(view: View) {

            //If its dynamicLinearLayout is invisible, make it visible and vice versa
            if (dynamicLinearLayout.visibility == View.GONE) {
                dynamicLinearLayout.visibility = View.VISIBLE
                setArrivalTimes()
            } else {
                dynamicLinearLayout.visibility = View.GONE
            }
            //update the itemView's dynamicLinearLayout visibility record in the stopsVisibility hashmap
            stopsVisibility[routeStopInfoItem.stopId] = dynamicLinearLayout.visibility
        }

        //setting the bus arrival times that are displayed in an itemView's dynamicLinearLayout
        fun setArrivalTimes() {
            //Obtaining an updated list of TripStops objects from the database that are associated with the itemView's stopId
            sharedViewModel.getArrivalTimes(routeStopInfoItem.stopId).observe(
                viewLifecycleOwner,
                { tripStop ->
                    if (tripStop.size > 1) {
                        var tmp = "" //temporary variable used to concatenate a list of arrival times to a single string

                        //sorting the list of TripStops objects using their arrivalTime attribute (Date object), from earliest to latest
                        val sortedTripStops = tripStop.sortedBy { it.arrivalTime }

                        //getting the index position in sortedTripStops that is associated with the tripStop with the earliest arrival time
                        // ... for the current stop using the itemView's stopId
                        val tripStopsPosition = tripStopsPositions[routeStopInfoItem.stopId]

                        //Using the tripStopsPosition and the next 4 consecutive integers as indexes, get the names of the tripStops in the
                        // ..sortedTripStops list with those indexes and add them to the tmp variable
                        for (i in tripStopsPosition!!..(tripStopsPosition + 4)) {
                            val tmpTripStop = sortedTripStops[i % sortedTripStops.size]
                            tmp += "${
                                tmpTripStop.arrivalTime.toString().substring(11, 16)
                            }......${tmpTripStop.stopSequence}\n"
                        }
                        allArrivalTimes.text = tmp //display the bus times inside of the textView housed within the itemView's dynamicLinearLayout
                    } else {
                        allArrivalTimes.text = "No Stop Times Found"
                    }
                }
            )
        }
    }

    private inner class StopAdapter(var routeStopInfoList: List<RouteStopInfo>)//accepts a list of RouteStops objects from model layer
        : RecyclerView.Adapter<StopsFragmentChild.StopHolder>(), Filterable {

        //This list will hold RouteStopInfo objects that have been filtered through a search query. It is initialized using ...
        // the original list of RouteStopInfo objects since no search queries have been made yet.
        private var filteredRouteStopInfoList: List<RouteStopInfo> = routeStopInfoList

        //creates a new viewHolder with a new itemView wrapped inside
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : StopsFragmentChild.StopHolder {

            //inflates the list_item_stop layout and passes the resulting View to a new instance of StopHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_stop, parent, false)
            return StopHolder(itemView)
        }

        //returns the number of items in the list of filtered RouteStopInfo objects
        override fun getItemCount() = filteredRouteStopInfoList.size

        //binds the viewHolder with a RouteStopInfo object from a given position in filteredRouteStopInfoList
        override fun onBindViewHolder(holder: StopsFragmentChild.StopHolder, position: Int) {
            val routeStop = filteredRouteStopInfoList[position]
            holder.bind(routeStop)
        }

        //creates a filter for the adapter.
        override fun getFilter(): Filter {
            return object : Filter() { //A filter constrains data with a filtering pattern

                //If the user has typed text into the SearchView, that text becomes a constraint to filter results from the list of RouteStopInfo objects
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val search = constraint.toString()
                    Log.d(TAG, search)

                    //if there is no search query, return all results from the unfiltered list of RouteStopInfo objects
                    if (search.isEmpty()) {
                        Log.d(TAG, "query empty")
                        filteredRouteStopInfoList = routeStopInfoList
                    } else {
                        val resultList: MutableList<RouteStopInfo> =
                            mutableListOf() //creating a list to temporarily store the results of the search query

                        //If a search query exists, check to see if it contains any of the names from the list of bus stops (RouteStopInfo objects).
                        //If a match is made to a bus stop, add that stop to the resultList
                        for (stop in routeStopInfoList) {
                            if (stop.name.lowercase(Locale.ROOT)
                                    .contains(search.lowercase(Locale.ROOT))
                            ) {
                                Log.d(TAG, "query match")
                                resultList.add(stop)
                            }
                        }
                        filteredRouteStopInfoList = resultList //update the filtered list of bus stops
                    }
                    //return the filtered list of RouteStopInfo object inside of a FilterResults object
                    val filteredResults = FilterResults()
                    filteredResults.values = filteredRouteStopInfoList
                    return filteredResults
                }

                //updating filteredRouteStopInfoList after each search query
                @Suppress("UNCHECKED_CAST")
                override fun publishResults(
                    constraint: CharSequence?,
                    results: FilterResults?
                ) {
                    filteredRouteStopInfoList = results?.values as List<RouteStopInfo>
                    notifyDataSetChanged()
                }

            }
        }
    }
    //WHEN THE FRAGMENT IS NO LONGER IN USE
    override fun onDestroy() {
        super.onDestroy()
        searchView.isIconified = false //close the searchView
    }
}
