package com.riis.etaDetroitkotlin

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.riis.etaDetroitkotlin.model.RouteStops
import com.riis.etaDetroitkotlin.model.Stops
import java.util.*

private const val TAG = "StopsFragment"
private const val DAY_KEY = "day_key"
private const val DIRECTIONS_KEY = "directions_key"

class StopsFragmentChild : Fragment() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var adapter: StopAdapter
    private lateinit var directionFab: FloatingActionButton
    private lateinit var noRoutesTextView: TextView
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var stopsVisibility: HashMap<Int, Int> = hashMapOf()
    private var tripStopsPositions: HashMap<Int, Int> = hashMapOf()
    private var day = 0
    private var directions: List<Int> = mutableListOf()
    private lateinit var routeStops: List<RouteStops>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        day = arguments?.getInt(DAY_KEY)!!
        directions = arguments?.getIntegerArrayList(DIRECTIONS_KEY)!!
        sharedViewModel.direction =
            if (directions.isNotEmpty()) directions[sharedViewModel.directionCount] else sharedViewModel.direction
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stops_child, container, false)

        stopsRecyclerView = view.findViewById(R.id.stops_recycler_view)
        directionFab = view.findViewById(R.id.fab)
        noRoutesTextView = view.findViewById(R.id.NoRouteLbl)
        stopsRecyclerView.layoutManager = LinearLayoutManager(context)

        setDirectionImage()
        setUpAppBar()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(day != 0 && sharedViewModel.direction != 0) {
            sharedViewModel.routeStopsListLiveData.observe(
                viewLifecycleOwner,
                { routeStops ->
                    this.routeStops = routeStops
                    updateUI(routeStops.filter {
                        it.directionId == sharedViewModel.direction && it.dayId == day
                    })
                }
            )
        }else{
            stopsRecyclerView.visibility = View.GONE
            directionFab.visibility = View.GONE
            noRoutesTextView.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        directionFab.setOnClickListener {

            sharedViewModel.direction = if (sharedViewModel.directionCount + 1 < directions.size) {
                directions[++sharedViewModel.directionCount] //go to next direction in list if list hasn't been exhausted
            } else {
                sharedViewModel.directionCount =
                    0 //if list has been exhausted go back to first element
                directions[sharedViewModel.directionCount]
            }

            setDirectionImage()
            updateUI(this.routeStops.filter { it.directionId == sharedViewModel.direction && it.dayId == day })
        }
    }


    private fun setDirectionImage() {
        var drawable = when (sharedViewModel.direction) {
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
            directionFab.visibility = View.GONE
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
        fun newInstance(day: Int, directions: ArrayList<Int>): StopsFragmentChild {
            val args = Bundle().apply {
                putInt(DAY_KEY, day)
                putIntegerArrayList(DIRECTIONS_KEY, directions)
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
        private val arrivalTimeLabel: TextView = view.findViewById(R.id.arrival_time_label)

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
                    val sortedTripStops = tripStop.sortedBy { it.arrivalTime }

                    if (sortedTripStops.size > 1) {
                        for (i in sortedTripStops.indices) {
                            val difference: Long =
                                sortedTripStops[i].arrivalTime?.time!! - Date(Calendar.getInstance().timeInMillis).time

                            if (difference > 0) {
                                val seconds = difference / 1000
                                val minutes = seconds / 60
                                currentTime.text = "(${
                                    sortedTripStops[i].arrivalTime.toString()
                                        .substring(11, 16)
                                })"

                                arrivalTimeLabel.text = "Next Stop: $minutes Minutes"
                                tripStopsPositions[stopItem.id] = i
                                break
                            }

                        }
                    } else {
                        currentTime.text = ""
                        arrivalTimeLabel.text = "No Stop Times Found"
                    }
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
                    if (tripStop.size > 1) {
                        var tmp = ""
                        val sortedTripStops = tripStop.sortedBy { it.arrivalTime }
                        val tripStopsPosition = tripStopsPositions[stopItem.id]

                        for (i in tripStopsPosition!!..(tripStopsPosition + 4)) {
                            val tmpTripStop = sortedTripStops[i % sortedTripStops.size]
                            tmp += "${
                                tmpTripStop.arrivalTime.toString().substring(11, 16)
                            }......${tmpTripStop.stopSequence}\n"
                        }
                        allArrivalTimes.text = tmp
                    } else {
                        allArrivalTimes.text = "No Stop Times Found"
                    }
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
