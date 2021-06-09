package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.Company
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.riis.etaDetroitkotlin.model.Routes

class RoutesFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var routeRecyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //RecyclerView setup (Grid Layout)
        routeRecyclerView = view.findViewById(R.id.company_recycler_view) as RecyclerView
        routeRecyclerView.layoutManager = LinearLayoutManager(context)
        //update the RecyclerView with itemViews and their corresponding data from the model layer
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeToRoutesSharedViewModel.routeListLiveData.observe(
            viewLifecycleOwner,
            { routes ->
                updateUI(routes)
            }
        )
    }

    private fun updateUI(routes: List<Routes>) {
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
        private val routeTitleTextView: TextView = itemView.findViewById(R.id.route_title)
        private val routeSubtitleTextView: TextView = itemView.findViewById(R.id.route_subtitle)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(route: Routes) {
            routeItem = route

            //updating the itemView attributes using the received data
            routeSubtitleTextView.text = "ROUTE ${routeItem.number}"
            routeTitleTextView.text = "ROUTE ${routeItem.number}"

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