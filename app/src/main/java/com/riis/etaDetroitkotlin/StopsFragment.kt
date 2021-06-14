package com.riis.etaDetroitkotlin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Stops
import com.riis.etaDetroitkotlin.model.TripStops
import org.w3c.dom.Text

private const val TAG = "StopsFragment"
private var CURRENT_DIRECTION: Int = 1
private var CURRENT_DAY: Int = 1

class StopsFragment : Fragment() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>
//    private lateinit var routeStopsList: List<RouteStops>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.stops_fragment, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "${sharedViewModel.getRouteName()}"

        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        val appBarColor = ColorDrawable(sharedViewModel.getCompany()?.brandColor?.toColorInt()!!)
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)

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
                updateUI(routeStops.filter {
                    it.directionId == CURRENT_DIRECTION && it.dayId == CURRENT_DAY
                })
            }
        )
    }

    private fun updateUI(routeStops: List<RouteStops>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }


    private inner class StopHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var stopItem: Stops
        private var allArrivalTimes: TextView = view.findViewById(R.id.all_arrival_times)


        private val stopName: TextView = view.findViewById(R.id.stop_name)
        private val currentTime: TextView = view.findViewById(R.id.current_time)

        private var dynamicLinearLayout =
            view.findViewById(R.id.dynamic_linear_layout) as LinearLayout

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
            allArrivalTimes.text = ""
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(stop: Stops) {
            stopItem = stop
            stopName.text = stopItem.name
            dynamicLinearLayout.visibility = View.GONE
            allArrivalTimes.text = null
            sharedViewModel.saveStop(stopItem)

            sharedViewModel.tripStopsListLiveData.observe(
                viewLifecycleOwner,
                { tripStop ->
                    currentTime.text = "(${tripStop.sortedBy {it.stopSequence}[0].arrivalTime.toString().substring(11, 16)})"
                }
            )
        }


        override fun onClick(view: View) {
            allArrivalTimes.text = null
            sharedViewModel.saveStop(stopItem)

            if (dynamicLinearLayout.visibility == View.GONE) {
                dynamicLinearLayout.visibility = View.VISIBLE
//                var tripStopCopy: List<TripStops> = null
                sharedViewModel.tripStopsListLiveData.observe(
                    viewLifecycleOwner,
                    { tripStop ->
                        for (i in tripStop.sortedBy {it.stopSequence}.subList(0, 5)) {
//                        for (i in tripStop.sortedWith(compareBy {it.stopSequence}, {it.arrivalTime} )) {
                            //TODO Fix bug where scrolling past view and scrolling back up gets rid of stuff
                            allArrivalTimes.text = "${allArrivalTimes.text}${i.arrivalTime.toString().substring(11, 16)}......${i.stopSequence}\n"
//                            tripStopCopy = tripStop
                        }
                    }
                )

//                Log.d(TAG, "$tripStopCopy")
            } else {
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
