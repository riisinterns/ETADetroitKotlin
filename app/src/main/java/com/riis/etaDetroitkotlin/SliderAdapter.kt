package com.riis.etaDetroitkotlin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.fragment.DirectionResponse
import com.riis.etaDetroitkotlin.fragment.GeneratedRoutes
import com.riis.etaDetroitkotlin.fragment.Steps


class SliderAdapter(private val routeGen: GeneratedRoutes) :
    RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.slider_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(routeGen.legs[0].steps[position])
    }

    override fun getItemCount(): Int {
        return routeGen.legs[0].steps.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val instructionsTextView: TextView =
            itemView.findViewById(R.id.instructionsTextView)
        private val distanceModalTextView: TextView =
            itemView.findViewById(R.id.distanceModalTextView)
        private val durationModalTextView: TextView =
            itemView.findViewById(R.id.durationModalTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)

        fun bind(step: Steps) {
            instructionsTextView.text = step.html_instructions
            distanceModalTextView.text = step.distance.text
            durationModalTextView.text = step.duration.text

            if (step.travel_mode == "WALKING") {
                detailsTextView.text = "Walk to given destination"
                instructionsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.round_directions_walk_black_36, 0, 0, 0)
            } else {
                instructionsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_directions_bus_black_36, 0, 0, 0)
                detailsTextView.text =
                    "${step.transit_details?.headsign}\ndeparting ${step.transit_details?.departure_time?.text}\narriving ${step.transit_details?.arrival_time?.text}\n(${step.transit_details?.num_stops} stops)"
            }
        }

    }
}