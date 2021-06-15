package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.Routes

class RoutesFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var busPhotoImageView: ImageView
    private var adapter: RouteAdapter? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var listOfCompanies: List<Company>


    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the fragment_home layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_routes, container, false)

        //RecyclerView setup (Grid Layout)
        routeRecyclerView = view.findViewById(R.id.route_recycler_view) as RecyclerView
        busPhotoImageView = view.findViewById(R.id.busImage) as ImageView
        routeRecyclerView.layoutManager = LinearLayoutManager(context)
        //update the RecyclerView with itemViews and their corresponding data from the model layer
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val currentCompany = sharedViewModel.currentCompany
        if (currentCompany != null) {
            updateUI(currentCompany)
        }

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )

        sharedViewModel.routeListLiveData.observe(
            viewLifecycleOwner,
            { routes ->
                updateRoutesDisplayed(routes)
            }
        )
    }


    override fun onPause() {
        super.onPause()
        val appBarColor =
            ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.ETAHeader))
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)
    }

    private fun updateRoutesDisplayed(routes: List<Routes>) {
        adapter = RouteAdapter(routes)
        routeRecyclerView.adapter = adapter
    }

    private fun updateUI(currentCompany: Company) {
        // set Photo and Background
        val resID: Int = requireContext().resources.getIdentifier(
            currentCompany.busImgUrl,
            "drawable",
            requireContext().packageName
        )
        busPhotoImageView.setImageResource(resID)
        routeRecyclerView.setBackgroundColor(Color.parseColor(currentCompany.brandColor))

        val appBarColor = ColorDrawable(Color.parseColor(currentCompany.brandColor))
        (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(appBarColor)

        when (currentCompany.name) {
            "SmartBus" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Smart Bus Route"
            "DDOT" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Ddot Bus Route"
            "FAST" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Fast Bus Route"
            "People Mover" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "People Mover Route"
            "QLine" -> (requireActivity() as AppCompatActivity).supportActionBar?.title =
                "Qline Route"
        }
    }

    //VIEW HOLDER CLASS FOR RECYCLER VIEW
    //-----------------------------------
    private inner class RouteHolder(view: View)
    //When given a view, it is used as a reusable blueprint for creating itemViews
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var routeItem: Routes //instantiating a new Route object to later receive model layer data

        //referencing the itemView's child views from the list_item_route layout
        private val routeNumberTextView: TextView = itemView.findViewById(R.id.route_number)
        private val routeNameTextView: TextView = itemView.findViewById(R.id.route_name)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(route: Routes) {
            routeItem = route

            //updating the itemView attributes using the received data

            routeNumberTextView.text =
                getString(R.string.route_item_subtitle, routeItem.number.toString())
            routeNameTextView.text = routeItem.name

        }

        override fun onClick(itemView: View) {
            //TODO navigate to StopsFragment
            Toast.makeText(
                context,
                "Clicked on route number ${routeItem.number}",
                Toast.LENGTH_SHORT
            ).show()
            sharedViewModel.saveRoute(routeItem)
            itemView.findNavController().navigate(R.id.route_to_stops)
        }
    }


    //ADAPTER CLASS FOR RECYCLER VIEW
    //-------------------------------
    private inner class RouteAdapter(var routeList: List<Routes>)//accepts a list of Route objects from model layer
        : RecyclerView.Adapter<RouteHolder>() {

        //creates a new viewHolder with a new itemView wrapped inside
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RouteHolder {
            //inflates the list_item_route layout and passes the resulting View to a new instance of RouteHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_route, parent, false)
            return RouteHolder(itemView)
        }

        //returns the number of items in the list of Route objects
        override fun getItemCount() = routeList.size

        //binds the viewHolder with a Route object from a given position in routeList
        override fun onBindViewHolder(holder: RouteHolder, position: Int) {
            val route = routeList[position]
            holder.bind(route)
        }
    }

}