package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.Routes
import java.util.*

class RoutesFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var busPhotoImageView: ImageView
    private var adapter: RouteAdapter? = null

    //links the fragment to a viewModel shared with MainActivity and other fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()


    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =  TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        //inflating the fragment_home layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_routes, container, false)

        //RecyclerView setup (Grid Layout)
        routeRecyclerView = view.findViewById(R.id.route_recycler_view) as RecyclerView
        busPhotoImageView = view.findViewById(R.id.busImgView) as ImageView
        routeRecyclerView.layoutManager = LinearLayoutManager(context)
        //update the RecyclerView with itemViews and their corresponding data from the model layer

        setHasOptionsMenu(true) //allows this fragment to be able to add its own menu options to the Main Activity's app bar

        return view
    }

    //CONNECTING FRAGMENT VIEW TO MODEL DATA
    //--------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //if a company was selected and saved to the shared view model from the HomeFragment, update fragment UI using that data
        val currentCompany = sharedViewModel.currentCompany
        if (currentCompany != null) {
            updateUI(currentCompany)
        }


        //listen to changes to the list of Routes objects saved in the database and update the recycler view using it
        sharedViewModel.routeListLiveData.observe(
            viewLifecycleOwner,
            { routes ->
                updateRoutesDisplayed(routes)
            }
        )
    }

    //WHEN THE FRAGMENT VIEW IS NOT VISIBLE ON THE SCREEN
    //---------------------------------------------------
    override fun onPause() {
        super.onPause()
        //When user leaves fragment, app bar color reverts back to its original color (green)
        val appBarColor =
            //creating a ColorDrawable from the home screen's color resource id
            ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.ETAHeader))
        //using the ColorDrawable to change the app bar color in MainActivity
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)
    }

    //when the list of Routes objects changes, update the recycler view
    private fun updateRoutesDisplayed(routes: List<Routes>) {
        adapter = RouteAdapter(routes)
        routeRecyclerView.adapter = adapter
    }

    //When the selected company from the HomeFragment changes, update the fragment's UI
    private fun updateUI(currentCompany: Company) {

        //getting the image resource id based on the selected Company object's busImgUrl property
        val resID: Int = context?.resources!!.getIdentifier(
            currentCompany.busImgUrl,
            "drawable",
            requireContext().packageName
        )
        busPhotoImageView.setImageResource(resID) //setting image view

        //setting the background color of the recycler view based on the selected Company object's brandColor property
        routeRecyclerView.setBackgroundColor(Color.parseColor(currentCompany.brandColor))

        //creating a ColorDrawable based on the selected Company's brandColor property
        val appBarColor = ColorDrawable(Color.parseColor(currentCompany.brandColor))
        //using the ColorDrawable to change the app bar color in MainActivity
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)

        //setting the title of the MainActivity's app bar based on the selected Company object's title property
        when (currentCompany.name) {
            "SmartBus" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Smart Bus Route"
            "DDOT" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Ddot Bus Route"
            "FAST" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Fast Bus Route"
            "People Mover" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "People Mover Route"
            "QLine" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Qline Route"
        }
    }

    //VIEW HOLDER CLASS FOR RECYCLER VIEW
    //-----------------------------------
    private inner class RouteHolder(view: View)
    //When given a view, it is used as a reusable blueprint for creating itemViews
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var routeItem: Routes //instantiating a new Route object to later receive model layer data

        //referencing the itemView's child views from the list_item_route layout
        private val routeNumberTextView: TextView = itemView.findViewById(R.id.route_number)
        private val routeNameTextView: TextView = itemView.findViewById(R.id.route_name)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(route: Routes) {
            routeItem = route

            //updating the itemView attributes using the received data

            routeNumberTextView.text =
                getString(R.string.route_item_subtitle, routeItem.number.toString())
            routeNameTextView.text = routeItem.name

        }

        //Handling navigation when an item view (route) is selected from the recycler view
        override fun onClick(itemView: View) {
            sharedViewModel.saveRoute(routeItem)
            itemView.findNavController().navigate(R.id.route_to_stops)
        }
    }


    //ADAPTER CLASS FOR RECYCLER VIEW
    //-------------------------------
    private inner class RouteAdapter(var routeList: List<Routes>)//accepts a list of Routes objects from model layer
        : RecyclerView.Adapter<RouteHolder>(), Filterable {

        //This list will hold Routes objects that have been filtered through a search query. It is initialized using ...
        // the original list of Routes objects since no search queries have been made yet.
        private var routeFilterList: List<Routes> = routeList

        //creates a new viewHolder with a new itemView wrapped inside
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RouteHolder {
            //inflates the list_item_route layout and passes the resulting View to a new instance of RouteHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_route, parent, false)
            return RouteHolder(itemView)
        }

        //returns the number of items in the list of filtered Routes objects
        override fun getItemCount() = routeFilterList.size

        //binds the viewHolder with a Routes object from a given position in routeFilterList
        override fun onBindViewHolder(holder: RouteHolder, position: Int) {
            val route = routeFilterList[position]
            holder.bind(route)
        }

        //creates a filter for the adapter.
        override fun getFilter(): Filter {
            return object : Filter() { //A filter constrains data with a filtering pattern

                //If the user has typed text into the SearchView, that text becomes a constraint to filter results from the list of Routes objects
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val search = constraint.toString()

                    //if there is no search query, return all results from the list of Routes objects
                    if (search.isEmpty()) {
                        routeFilterList = routeList
                    } else {
                        val resultList: MutableList<Routes> = mutableListOf()

                        //If a search query exists, check to see if it contains any of the names or route numbers from the list of Routes object.
                        //If a match is made to a route, add that route to the resultList
                        for (route in routeList) {
                            val routeNumber: String = "route " + route.number
                            if (route.name.lowercase(Locale.ROOT)
                                    .contains(search.lowercase(Locale.ROOT))
                                || routeNumber.lowercase(Locale.ROOT).trim()
                                    .contains(search.lowercase(Locale.ROOT))
                            ) {
                                resultList.add(route)
                            }
                        }
                        routeFilterList = resultList //update the filtered list of routes
                    }
                    //return the filtered list of Routes object inside of a FilterResults object
                    val filteredResults = FilterResults()
                    filteredResults.values = routeFilterList
                    return filteredResults
                }

                //updating routeFilterList after each search query
                @Suppress("UNCHECKED_CAST")
                override fun publishResults(
                    constraint: CharSequence?,
                    results: FilterResults?
                ) {
                    routeFilterList = results?.values as List<Routes>
                    notifyDataSetChanged()
                }

            }
        }
    }

    //ADDING MENU OPTIONS TO THE APP BAR PROVIDED BY MAIN ACTIVITY
    //------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            R.menu.search_menu,
            menu
        ) //search_menu.xml displays a search icon (magnifying glass) in the top right of the app bar


        /*NOTE: An action view is an action that provides functionality within the app bar. In this case, we ...
                are using the SearchView action view which initially appears as a menu item. When the user clicks ...
                the action, it expands to fit the app bar.

                A SearchView allows the user to enter search query and submit a request to a search provider.
                It Shows a list of query suggestions or results, if available, and allows the user to pick a suggestion or result to launch into.
         */

        //creating and configuring the search bar
        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView =
            searchIcon?.actionView as SearchView //SearchView widget implements an action view for entering search queries
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
                adapter?.filter?.filter(s)
                return false
            }
        })

    }
}
