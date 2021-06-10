package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.riis.etaDetroitkotlin.model.Routes

class RoutesFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var busPhotoImageView: ImageView
    private var adapter: RouteAdapter? = null
    private val homeToRoutesSharedViewModel: HomeToRoutesSharedViewModel by activityViewModels()

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

        // set Photo and Background
        val currentCompany = homeToRoutesSharedViewModel.currentCompany
        val resID: Int = context?.resources!!.getIdentifier(currentCompany?.busImgUrl, "drawable", requireContext().packageName)
        busPhotoImageView.setImageResource(resID)
        routeRecyclerView.setBackgroundColor(Color.parseColor(currentCompany?.brandColor))



        homeToRoutesSharedViewModel.routeListLiveData.observe(
            viewLifecycleOwner,
            { routes ->
                updateRoutesDisplayed(routes)
            }
        )
    }

    private fun updateRoutesDisplayed(routes: List<Routes>) {
        adapter = RouteAdapter(routes)
        routeRecyclerView.adapter = adapter
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

            routeNumberTextView.text = getString(R.string.route_item_subtitle, routeItem.number.toString())
            routeNameTextView.text = routeItem.name

        }

        override fun onClick(itemView: View) {
            //TODO navigate to StopsFragment
        }
    }

    //ADAPTER CLASS FOR RECYCLER VIEW
    //-------------------------------
    private inner class RouteAdapter(var routeList: List<Routes>)//accepts a list of Company objects from model layer
        : RecyclerView.Adapter<RouteHolder>() {

        //creates a new viewHolder with a new itemView wrapped inside
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RouteHolder {
            //inflates the list_item_transport layout and passes the resulting View to a new instance of CompanyHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_route, parent, false)
            return RouteHolder(itemView)
        }

        //returns the number of items in the list of Transport objects
        override fun getItemCount() = routeList.size

        //binds the viewHolder with a Company object from a given position in companyList
        override fun onBindViewHolder(holder: RouteHolder, position: Int) {
            val route = routeList[position]
            holder.bind(route)
        }
    }

}