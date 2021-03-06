package com.riis.etaDetroitkotlin.fragment

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.model.Company

private const val TAG = "DEBUG"

private val checkBoxCompanyNames: MutableMap<Int, String> = HashMap()
private var busRoutes: MutableMap<String, GeoJsonLayer> = HashMap()
private lateinit var progressDialog: RouteLoadingDialog

class RouteMapFragment : Fragment(), View.OnClickListener {

    private lateinit var listOfCompanies: List<Company>
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private val callback = OnMapReadyCallback { googleMap ->
        val isDarkThemeOn =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK === Configuration.UI_MODE_NIGHT_YES
        var mapTheme = R.raw.light_mode_map
        if (isDarkThemeOn) mapTheme = R.raw.dark_mode_map

        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context as Context,
                    mapTheme
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

        val detroit = LatLng(42.3482862, -83.068969)
        googleMap.addMarker(MarkerOptions().position(detroit).title("Marker in Detroit"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(detroit, 10F))

        try {
            addBusRoute(googleMap, "Ddot Bus Route", R.raw.ddot_routes, R.color.DdotGreen)
            addBusRoute(googleMap, "Smart Bus Route", R.raw.smart_bus_routes, R.color.SmartBusRed)
            addBusRoute(googleMap, "Fast Bus Route", R.raw.fast_routes, R.color.ReflexBlue)
            addBusRoute(
                googleMap,
                "Detroit People Mover",
                R.raw.people_mover_routes,
                R.color.PeopleMoverColor
            )
            addBusRoute(googleMap, "QLine", R.raw.qline, R.color.Qline)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "Failed to load route map", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.clearDirectionResponse()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_route_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val ddotCheckbox = view.findViewById<CheckBox>(R.id.DdotCheckBox)
        val smartCheckbox = view.findViewById<CheckBox>(R.id.SmartCheckBox)
        val reflexCheckbox = view.findViewById<CheckBox>(R.id.ReflexCheckBox)
        val peopleMoverCheckbox = view.findViewById<CheckBox>(R.id.PeopleMoverCheckBox)
        val qlineCheckbox = view.findViewById<CheckBox>(R.id.QlineCheckBox)

        checkBoxCompanyNames[R.id.DdotCheckBox] = "Ddot Bus Route"
        checkBoxCompanyNames[R.id.SmartCheckBox] = "Smart Bus Route"
        checkBoxCompanyNames[R.id.ReflexCheckBox] = "Fast Bus Route"
        checkBoxCompanyNames[R.id.QlineCheckBox] = "QLine"
        checkBoxCompanyNames[R.id.PeopleMoverCheckBox] = "Detroit People Mover"

        ddotCheckbox.setOnClickListener(this)
        smartCheckbox.setOnClickListener(this)
        reflexCheckbox.setOnClickListener(this)
        qlineCheckbox.setOnClickListener(this)
        peopleMoverCheckbox.setOnClickListener(this)

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )
    }

    class LayerThread(private val layer: GeoJsonLayer, private val pd: RouteLoadingDialog) :
        Runnable {
        override fun run() {
            layer.addLayerToMap()
            pd.dismiss()
        }
    }

    private fun showLayer(layer: GeoJsonLayer, pd: RouteLoadingDialog) {
        val thread = LayerThread(layer, pd)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(thread, 1000)
    }

    override fun onClick(v: View?) {
        val layer = busRoutes[checkBoxCompanyNames[v?.id]]
        val box = v?.findViewById<CheckBox>(v.id)

        if (box != null) {
            if (box.isChecked) {
                checkBoxCompanyNames[v.id]?.let {
                    val fragmentManager = fragmentManager

                    progressDialog = RouteLoadingDialog(it)
                    fragmentManager?.let { it1 -> progressDialog.show(it1, it) }
                }

                showLayer(layer!!, progressDialog)
            } else {
                layer!!.removeLayerFromMap()
            }
        }
    }

    private fun addBusRoute(map: GoogleMap, companyName: String, resourceId: Int, colorId: Int) {
        val layer = GeoJsonLayer(map, resourceId, context)
        layer.defaultLineStringStyle.color = context?.let { ContextCompat.getColor(it, colorId) }!!
        busRoutes[companyName] = layer
    }
}