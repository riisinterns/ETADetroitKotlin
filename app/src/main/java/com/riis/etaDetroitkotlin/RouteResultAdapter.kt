package com.riis.etaDetroitkotlin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.fragment.BottomSheetDialog
import com.riis.etaDetroitkotlin.fragment.DirectionResponse
import com.riis.etaDetroitkotlin.fragment.GeneratedRoutes


class RouteResultAdapter(private val directionResponse: DirectionResponse) :
    RecyclerView.Adapter<RouteResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteResultViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.route_result_row, parent, false)
        return RouteResultViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RouteResultViewHolder, position: Int) {
        val routeGenerated = directionResponse.routes[position]
        holder.bind(routeGenerated)
    }

    override fun getItemCount(): Int {
        return directionResponse.routes.count()
    }
}

class RouteResultViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener  {
    private lateinit var routeGen: GeneratedRoutes
    private val transitDetailsTextView: TextView =
        itemView.findViewById(R.id.transitDetailsTextView)
    private val timeFrameTextView: TextView = itemView.findViewById(R.id.timeFrameTextView)
    private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)
    private val fareTextView: TextView = itemView.findViewById(R.id.fareTextView)
    private val distanceTextView: TextView = itemView.findViewById(R.id.distanceTextView)
    private val agencyLineTextView: TextView = itemView.findViewById(R.id.agencyLineTextView)

    @SuppressLint("SetTextI18n")
    fun bind(routeGen: GeneratedRoutes) {
        this.routeGen = routeGen

        for (step in this.routeGen.legs[0].steps) {
            if (step.travel_mode == "TRANSIT") {
                transitDetailsTextView.text =
                    "${step.transit_details?.line?.short_name} ${step.html_instructions}"
                agencyLineTextView.text = step.transit_details?.line!!.agencies[0].name
                timeFrameTextView.text =
                    "${step.transit_details.departure_time.text} - ${this.routeGen.legs[0].arrival_time.text}"
            }
        }

        durationTextView.text = this.routeGen.legs[0].duration.text
        if (this.routeGen.fare != null) fareTextView.text =
            this.routeGen.fare?.text else fareTextView.text = "0.00"
        distanceTextView.text = this.routeGen.legs[0].distance.text

    }

    init {
        itemView.setOnClickListener(this) //setting a click listener on each itemView
    }

    override fun onClick(v: View?) {
        Toast.makeText(itemView.context, "Apple", Toast.LENGTH_SHORT).show()
        BottomSheetDialog().show((itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction(), "exampleBottomSheet")
    }
}