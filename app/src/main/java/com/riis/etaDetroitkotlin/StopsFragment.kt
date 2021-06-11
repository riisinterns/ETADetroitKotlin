package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Routes
import com.riis.etaDetroitkotlin.model.Stops

private const val TAG = "StopsFragment"

class StopsFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>
    private lateinit var navController: NavController
    private lateinit var drawerMenu: DrawerLayout
//    private lateinit var routeStopsList: List<RouteStops>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.stops_fragment, container, false)

        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        drawerMenu = requireActivity().findViewById(R.id.drawer_menu)
        val navView = activity?.findViewById<NavigationView>(R.id.side_nav_view)
        navView?.setupWithNavController(navController)
        navView?.setNavigationItemSelectedListener(this)

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )

        sharedViewModel.routeStopsListLiveData.observe(
            viewLifecycleOwner,
            { routeStops ->
                updateUI(routeStops)
            }
        )
    }

    private fun updateUI(routeStops: List<RouteStops>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerMenu.closeDrawers()

        Handler(Looper.getMainLooper()).postDelayed({
            when (item.itemId) {

                R.id.nav_home-> navController.navigate(R.id.stopsFragment_to_homeFragment)

                R.id.nav_route_map -> navController.navigate(R.id.stopsFragment_to_routeMapFragment)

                R.id.nav_ddot ->{
                    sharedViewModel.saveCompany(listOfCompanies[1])
                    navController.navigate(R.id.stopsFragment_to_routesFragment)
                }
                R.id.nav_smart -> {
                    sharedViewModel.saveCompany(listOfCompanies[0])
                    navController.navigate(R.id.stopsFragment_to_routesFragment)
                }
                R.id.nav_reflex -> {
                    sharedViewModel.saveCompany(listOfCompanies[2])
                    navController.navigate(R.id.stopsFragment_to_routesFragment)
                }
                R.id.nav_people_mover -> {
                    sharedViewModel.saveCompany(listOfCompanies[3])
                    navController.navigate(R.id.stopsFragment_to_routesFragment)
                }
                R.id.nav_qline -> {
                    sharedViewModel.saveCompany(listOfCompanies[4])
                    navController.navigate(R.id.stopsFragment_to_routesFragment)
                }
                R.id.nav_planner -> {
                    navController.navigate(R.id.stopsFragment_to_routePlannerFragment)
                }
            }
        }, 500)

        return true
    }

    private inner class StopHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var stopItem: Stops
        private var allArrivalTimes: TextView = itemView.findViewById(R.id.all_arrival_times)


        private val stopName: TextView = itemView.findViewById(R.id.stop_name)

        private val dynamicLinearLayout = itemView.findViewById(R.id.dynamic_linear_layout) as LinearLayout

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
            allArrivalTimes.text = ""
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(stop: Stops) {
            stopItem = stop
            stopName.text = stopItem.name

        }

        override fun onClick(itemView: View) {
            Toast.makeText(context, "Clicked on stop  ${stopItem.name}", Toast.LENGTH_SHORT).show()
            sharedViewModel.saveStop(stopItem)

            if (dynamicLinearLayout.visibility == View.GONE) {
                dynamicLinearLayout.visibility = View.VISIBLE

                sharedViewModel.tripStopsListLiveData.observe(
                    viewLifecycleOwner,
                    { tripStop ->
                        for (i in tripStop) {
                            Log.d(TAG, "TIME FOR ${stopItem.name}: ${i.arrivalTime}")
//                           allArrivalTimes.text = allArrivalTimes.text + i.arrivalTime
                        }
                    }
                )

            } else{
                dynamicLinearLayout.visibility = View.GONE
            }
        }
    }

    private inner class StopAdapter(var routeStopsList: List<RouteStops>)//accepts a list of Company objects from model layer
        : RecyclerView.Adapter<StopsFragment.StopHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : StopsFragment.StopHolder {
            val itemView = layoutInflater.inflate(R.layout.stop_list_item, parent, false)
            return StopHolder(itemView)
        }

        override fun getItemCount() = routeStopsList.size

        override fun onBindViewHolder(holder: StopsFragment.StopHolder, position: Int) {
            val routeStop = routeStopsList[position]

            sharedViewModel.getStop(routeStop.stopId).observe(
                viewLifecycleOwner,
                { stop ->
                    holder.bind(stop)
                }
            )
        }
    }
}
