package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


//HomeFragment is a fragment that displays a grid-based RecyclerView
//It provides the interface for the user to select between different bus companies

class HomeFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var companyRecyclerView: RecyclerView
    private var adapter: CompanyAdapter? = null
    
    //LINKING FRAGMENT WITH A VIEW MODEL
    //----------------------------------
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the fragment_transport_list layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //RecyclerView setup (Grid Layout)
        companyRecyclerView = view.findViewById(R.id.company_recycler_view) as RecyclerView
        companyRecyclerView.layoutManager = GridLayoutManager(context,2) //second parameter specifies number of columns in grid

        //update the RecyclerView with itemViews and their corresponding data from the model layer
        updateUI()
        return view
    }

    //UPDATING THE FRAGMENT VIEW
    //--------------------------
    private fun updateUI() {
        //creating an adapter and connecting it to the model layer data
        val companyList = homeViewModel.companyList
        adapter = CompanyAdapter(companyList)

        //Connecting the RecyclerView to its adapter
        companyRecyclerView.adapter = adapter

    }

    //VIEW HOLDER CLASS FOR RECYCLER VIEW
    //-----------------------------------
    private inner class CompanyHolder(view: View)
        //When given a view, it is used as a reusable blueprint for creating itemViews
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var companyItem: Company //instantiating a new Company object to later receive model layer data

        //referencing the itemView's child views from the list_item_transport layout
        private val companyNameTextView: TextView = itemView.findViewById(R.id.company_name)
        private val companyImageView: ImageView = itemView.findViewById(R.id.company_image)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to another from the model layer
        fun bind(company: Company) {
            companyItem = company

            //updating the itemView attributes using the received data
            companyNameTextView.text = companyItem.name

            when (companyItem.name){
                "DDOT" -> {
                    companyImageView.setImageResource(R.drawable.ddot_bus)
                    companyNameTextView.setBackgroundResource(R.color.DdotGreen)
                }
                "People Mover" -> {
                    companyImageView.setImageResource(R.drawable.people_mover)
                    companyNameTextView.setBackgroundResource(R.color.PeopleMoverColor)
                }
                "QLine" -> {
                    companyImageView.setImageResource(R.drawable.qline)
                    companyNameTextView.setBackgroundResource(R.color.Qline)
                }
                "RefleX" -> {
                    companyImageView.setImageResource(R.drawable.reflex)
                    companyNameTextView.setBackgroundResource(R.color.ReflexBlue)
                }
                "SmartBus" -> {
                    companyImageView.setImageResource(R.drawable.smart)
                    companyNameTextView.setBackgroundResource(R.color.SmartBusRed)
                }
                "Route Map" -> {
                    companyImageView.setImageResource(R.drawable.route_map_card_image)
                    companyNameTextView.setBackgroundResource(R.color.PlanMyRoute)
                }
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${companyItem.name} pressed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //ADAPTER CLASS FOR RECYCLER VIEW
    //-------------------------------
    private inner class CompanyAdapter(var companyList: List<Company>)//accepts a list of Company objects from model layer
        : RecyclerView.Adapter<CompanyHolder>() {

        //creates a new viewHolder with a new itemView wrapped inside
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CompanyHolder {
            //inflates the list_item_transport layout and passes the resulting View to a new instance of CompanyHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_transport, parent, false)
            return CompanyHolder(itemView)
        }

        //returns the number of items in the list of Transport objects
        override fun getItemCount() = companyList.size

        //binds the viewHolder with a Company object from a given position in companyList
        override fun onBindViewHolder(holder: CompanyHolder, position: Int) {
            val company = companyList[position]
            holder.bind(company)
        }
    }

    //FUNCTION THAT ACTIVITIES CAN CALL TO GET AN INSTANCE OF THE FRAGMENT
    //--------------------------------------------------------------------
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }



}


