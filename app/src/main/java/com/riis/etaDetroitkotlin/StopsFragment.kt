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

class StopsFragment : Fragment(){

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>
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


//        val stop: Stops = Stops(1, "Lafayette & Trumbull", 1.0, 2.0)
//
//        val test: List<Stops> = listOf(stop, Stops(1, "Miller @ Ford Gate 6", 1.0, 2.0))
//
//        updateUI(test)
    }

    private fun updateUI(routeStops: List<RouteStops>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }



    private inner class StopHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var stopItem: Stops

        private val stopName: TextView = itemView.findViewById(R.id.stop_name)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(stop: Stops) {
            stopItem = stop
            stopName.text = stopItem.name

        }

        override fun onClick(itemView: View) {
            //TODO navigate to StopsFragment
//            Toast.makeText(context, "Clicked on route number ${routeItem.number}", Toast.LENGTH_SHORT).show()
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
//            holder.bind(stop)
        }
    }
}
