package com.riis.etaDetroitkotlin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.GsonBuilder
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.RouteResultAdapter
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.model.Company
import okhttp3.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DirectionResponse(val routes: List<GeneratedRoutes>)
class GeneratedRoutes(val copyrights: String, val fare: TextData? = null, val legs: List<Legs>)
class Legs(val arrival_time: TextData, val departure_time: TextData, val distance: TextData, val duration: TextData, val steps: List<Steps>)
class Steps(val distance: TextData, val duration: TextData, val html_instructions: String, val travel_mode: String, val transit_details: TransitDetails? = null)
class TransitDetails(val departure_time: TextData, val arrival_time: TextData, val num_stops: String, val line: Line)
class Line(val short_name: String)
class TextData(val text: String);

class RoutePlannerFragment : Fragment() {
    var routesRecyclerView: RecyclerView? = null
    private lateinit var listOfCompanies: List<Company>
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var getRouteButton: Button
    private lateinit var departureLocationQuery: AutoCompleteTextView
    private lateinit var arrivalLocationQuery: AutoCompleteTextView
    private lateinit var copyrightTextView: TextView
    private var currentDate: String = " "
    private var currentTime: String = " "
    private var apiKey: String = "AIzaSyBVQ6Je04mW9TapTvsJoGOn5xVrw2xaFAY"
    private val TAG = "DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_planner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateButton = view.findViewById(R.id.dateButton)
        timeButton = view.findViewById(R.id.timeButton)
        getRouteButton = view.findViewById(R.id.getRouteButton)
        copyrightTextView = view.findViewById(R.id.copyrightTextView)
        routesRecyclerView = view.findViewById<View>(R.id.routesRecyclerView) as RecyclerView

        departureLocationQuery = view.findViewById(R.id.fromField)
        arrivalLocationQuery = view.findViewById(R.id.toField)

        dateButton.setOnClickListener { (openDatePicker()) }
        timeButton.setOnClickListener { (openTimePicker()) }
        getRouteButton.setOnClickListener {
//            Toast.makeText(context, "from ${departureLocationQuery.text} to ${arrivalLocationQuery.text}", Toast.LENGTH_LONG).show()
//            Log.i(TAG, "from ${departureLocationQuery.text} to ${arrivalLocationQuery.text}")

            val time = "$currentDate $currentTime"
//            Log.i(TAG, "$time ----> ${milliseconds(time)}")

            getApiDirectionData(departureLocationQuery.text.toString(), arrivalLocationQuery.text.toString(), milliseconds(time).toString(), apiKey)
            routesRecyclerView!!.layoutManager = LinearLayoutManager(context)

        }

        departureLocationQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //if there is some text in the field do this
                if (s!!.isNotEmpty()) autocompleteLocation(s.toString(), departureLocationQuery)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        arrivalLocationQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //if there is some text in the field do this
                if (s!!.isNotEmpty()) autocompleteLocation(s.toString(), arrivalLocationQuery)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    @SuppressLint("SimpleDateFormat")
    private fun milliseconds(date: String): Long {
        val parser = SimpleDateFormat("MMM dd, yyyy HH:mm")
        try {
            val mDate = parser.parse(date)

            return mDate!!.time / 1000
        } catch (e: ParseException) {
            Log.i(TAG, "SORRY IT DIDN'T WORK")
        }
        return 0
    }

    private fun getApiDirectionData(
        origin: String,
        destination: String,
        departureTime: String,
        key: String
    ): String {
        var url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${origin}&destination=${destination}&departure_time=${departureTime}&mode=transit&alternatives=true&key=${key}"

        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.i(TAG, "FAILED TO EXECUTE DIRECTIONS REQUEST")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
//                if (body != null) Log.i(TAG, body)
                val gson = GsonBuilder().create()

                val directionResponse = gson.fromJson(body, DirectionResponse::class.java)


                activity?.runOnUiThread{
                    copyrightTextView.text = directionResponse.routes[0].copyrights // required by Google to use their api
                    routesRecyclerView!!.adapter = RouteResultAdapter(directionResponse)
                }
            }

        })

        return ""
    }

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val calendar = Calendar.getInstance()
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE)).setTitleText("Time of Departure")
            .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
            .build()

        picker.show(childFragmentManager, "pick the time of departure")

        picker.addOnPositiveButtonClickListener {
            currentTime = "${picker.hour}:${picker.minute}"
            Toast.makeText(context, currentTime, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(childFragmentManager, "pick the day of departure");

        datePicker.addOnPositiveButtonClickListener {
            currentDate = "${datePicker.headerText}"
            Toast.makeText(context, currentDate, Toast.LENGTH_SHORT).show()
        }
    }

    private fun autocompleteLocation(query: String, field: AutoCompleteTextView) {

        if (!Places.isInitialized()) {
            context?.let { Places.initialize(it, apiKey) };
        }
        val placesClient = context?.let { Places.createClient(it) }
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        // Create a RectangularBounds object.
        val bounds = RectangularBounds.newInstance(
            LatLng(42.3482862, -83.068969),
            LatLng(42.35, -83.1)
        )
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(LatLng(42.3482862, -83.068969))
                .setCountries("US", "CA")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build()
        placesClient?.findAutocompletePredictions(request)
            ?.addOnSuccessListener { response: FindAutocompletePredictionsResponse ->

                var list: MutableMap<String, String> = mutableMapOf()

                for (prediction in response.autocompletePredictions) {
                    list["${prediction.getPrimaryText(null)} ${
                        prediction.getSecondaryText(null).toString()
                    }"] = prediction.placeId
//                    Log.i(TAG, list.toString())
                }

                val adapter =
                    ArrayAdapter(requireContext(), R.layout.list_item, ArrayList(list.keys))
                (field as? AutoCompleteTextView)?.setAdapter(adapter)

            }?.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: " + exception.statusCode)
                }
            }
    }


}