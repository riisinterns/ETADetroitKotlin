package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Routes
import com.riis.etaDetroitkotlin.model.Stops

private const val TAG = "StopsFragment"

class StopsFragment : Fragment() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

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
