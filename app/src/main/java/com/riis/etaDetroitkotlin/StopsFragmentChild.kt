package com.riis.etaDetroitkotlin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

private const val TAG = "StopsFragment"
private const val DAY_KEY = "day_key"
private var CURRENT_DIRECTION: Int = 1

class StopsFragmentChild : Fragment() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>
    private var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.day = arguments?.getInt(DAY_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stops_child, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "${sharedViewModel.currentRoute?.name}"

        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        val appBarColor = ColorDrawable(sharedViewModel.currentCompany?.brandColor?.toColorInt()!!)
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
                    it.directionId == CURRENT_DIRECTION && it.dayId == day
                })
            }
        )
    }

    private fun updateUI(routeStops: List<RouteStops>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }

    companion object{
        fun newInstance(day: Int): StopsFragmentChild{
            val args = Bundle().apply {
                putInt(DAY_KEY, day)
            }
            return StopsFragmentChild().apply {
                arguments = args
            }
        }
    }

    private inner class StopHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var stopItem: Stops
        private var allArrivalTimes: TextView = view.findViewById(R.id.all_arrival_times)


        private val stopName: TextView = view.findViewById(R.id.stop_name)

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
        }


        override fun onClick(view: View) {
            allArrivalTimes.text = ""
            sharedViewModel.saveStop(stopItem)

            if (dynamicLinearLayout.visibility == View.GONE) {
                dynamicLinearLayout.visibility = View.VISIBLE

                sharedViewModel.tripStopsListLiveData.observe(
                    viewLifecycleOwner,
                    { tripStop ->
                        for (i in tripStop) {
                            //TODO Fix bug where scrolling past view and scrolling back up gets rid of stuff
                            allArrivalTimes.text = "${allArrivalTimes.text}${i.arrivalTime}\n"
                        }
                    }
                )
            } else {
                dynamicLinearLayout.visibility = View.GONE
            }
        }
    }

    private inner class StopAdapter(var routeStopsList: List<RouteStops>)//accepts a list of RouteStops objects from model layer
        : RecyclerView.Adapter<StopsFragmentChild.StopHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : StopsFragmentChild.StopHolder {
            val itemView = layoutInflater.inflate(R.layout.stop_list_item, parent, false)
            return StopHolder(itemView)
        }

        override fun getItemCount() = routeStopsList.size

        override fun onBindViewHolder(holder: StopsFragmentChild.StopHolder, position: Int) {
            val routeStop = routeStopsList[position]
            sharedViewModel.getStopLiveData(routeStop.stopId).observe(
                viewLifecycleOwner,
                { stop ->
                    holder.bind(stop)
                }
            )
        }
    }
}
