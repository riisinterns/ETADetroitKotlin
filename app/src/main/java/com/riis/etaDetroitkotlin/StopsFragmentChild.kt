package com.riis.etaDetroitkotlin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Stops
import java.lang.Integer.max

private const val TAG = "StopsFragment"
private const val DAY_KEY = "day_key"
private const val DIRECTION_KEY = "direction_key"
private var CURRENT_DIRECTION: Int = 1

class StopsFragmentChild private constructor() : Fragment() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private lateinit var directionFab: FloatingActionButton
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>
    private var stopsVisibility: HashMap<Int, Int> = hashMapOf()
    private var day = 0
    private var direction = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //not null, only can create fragment by calling NewInstance()
        this.day = arguments?.getInt(DAY_KEY)!!
        this.direction = arguments?.getInt(DIRECTION_KEY)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stops_child, container, false)

        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        directionFab = view.findViewById(R.id.fab)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        setDirectionImage()
        setUpAppBar()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.routeStopsListLiveData.observe(
            viewLifecycleOwner,
            { routeStops ->
                updateUI(routeStops.filter {
                    it.directionId == direction && it.dayId == day
                })
            }
        )
    }

    private fun setDirectionImage() {
        var drawable = when (direction) {
            1 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
            2 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up)
            3 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_left)
            4 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_right)
            else -> null
        }

        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(requireContext(), R.color.white)
            )
            directionFab.setImageDrawable(drawable)
        } else {
            directionFab.visibility =
                View.INVISIBLE //use the fab in constraint layout, cannot set to gone
            directionFab.isEnabled = false
        }
    }

    private fun updateUI(routeStops: List<RouteStops>) {
        adapter = StopAdapter(routeStops)
        stopsRecyclerView.adapter = adapter
    }

    private fun setUpAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            "${sharedViewModel.currentRoute?.name}"

        val appBarColor = ColorDrawable(sharedViewModel.currentCompany?.brandColor?.toColorInt()!!)
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)
    }


    companion object {
        fun newInstance(day: Int, direction: Int): StopsFragmentChild {
            val args = Bundle().apply {
                putInt(DAY_KEY, day)
                putInt(DIRECTION_KEY, direction)
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
        private val currentTime: TextView = view.findViewById(R.id.current_time)

        private var dynamicLinearLayout =
            view.findViewById(R.id.dynamic_linear_layout) as LinearLayout

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
            dynamicLinearLayout.visibility = View.GONE
        }

        //binding the viewHolder's Stop object to date of another from the model layer
        fun bind(stop: Stops) {
            stopItem = stop
            stopName.text = stopItem.name
            allArrivalTimes.text = null

            if (stopItem.id !in stopsVisibility) {
                stopsVisibility[stopItem.id] = View.GONE
            }
            dynamicLinearLayout.visibility = stopsVisibility[stopItem.id]!!

            if (dynamicLinearLayout.visibility == View.VISIBLE) {
                setArrivalTimes()
            }

            sharedViewModel.getTripStops(stopItem.id).observe(
                viewLifecycleOwner,
                { tripStop ->
                    currentTime.text = "(${
                        tripStop.sortedBy { it.stopSequence }[0].arrivalTime.toString()
                            .substring(11, 16)
                    })"
                }
            )
        }

        override fun onClick(view: View) {
            allArrivalTimes.text = null

            if (dynamicLinearLayout.visibility == View.GONE) {
                dynamicLinearLayout.visibility = View.VISIBLE
                setArrivalTimes()
            } else {
                dynamicLinearLayout.visibility = View.GONE
            }

            stopsVisibility[stopItem.id] = dynamicLinearLayout.visibility
        }

        fun setArrivalTimes() {
            sharedViewModel.getTripStops(stopItem.id).observe(
                viewLifecycleOwner,
                { tripStop ->
                    var tmp = ""
                    for (i in tripStop.sortedBy { it.stopSequence }.subList(0, minOf(tripStop.size, 5))) {
//                        for (i in tripStop.sortedWith(compareBy {it.stopSequence}, {it.arrivalTime} )) {
                        tmp += "${
                            i.arrivalTime.toString().substring(11, 16)
                        }......${i.stopSequence}\n"
                    }
                    allArrivalTimes.text = tmp
                }
            )
        }

    }

    private inner class StopAdapter(var routeStopsList: List<RouteStops>)//accepts a list of RouteStops objects from model layer
        : RecyclerView.Adapter<StopsFragmentChild.StopHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : StopsFragmentChild.StopHolder {
            val itemView = layoutInflater.inflate(R.layout.list_item_stop, parent, false)
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
