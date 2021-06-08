package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "TransportListFragment"

//TransportListFragment is a fragment that displays a grid-based RecyclerView when hosted by MainActivity.kt
//The RecyclerView is used to display a grid of transportation options the user can choose from on the screen

class TransportListFragment : Fragment() {

    //DEFINING CLASS VARIABLES
    //------------------------
    private lateinit var transportRecyclerView: RecyclerView
    private var adapter: TransportAdapter? = null
    
    //CONNECTING THE FRAGMENT TO A VIEW MODEL
    //---------------------------------------
    private val transportListViewModel: TransportListViewModel by lazy {
        ViewModelProviders.of(this).get(TransportListViewModel::class.java)
    }

    //CREATING THE FRAGMENT
    //---------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //creating a new fragment using whatever data is stored in savedInstanceState

        //logs the number of Transport objects stored in TransportListViewModel.kt
        Log.d(TAG, "Total Transport objects: ${transportListViewModel.transportList.size}")
    }

    //Creating AND INFLATING THE FRAGMENT VIEW AND CONFIGURING ITS RecyclerView
    //-------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater, //LayoutInflater converts Layout-defining XML files into View objects
        container: ViewGroup?, //the parent view that the fragment's UI should be attached to
        savedInstanceState: Bundle?
    ): View? {
        //inflating the layout fragment_transport_list.xml into a view
        val view = inflater.inflate(R.layout.fragment_transport_list, container, false)

        //referencing the RecyclerView from the layout using its View ID
        transportRecyclerView = view.findViewById(R.id.transport_recycler_view) as RecyclerView

        //giving the RecyclerView a LayoutManager which is required to make it work
        //GridLayoutManager arranges item views in a grid on the screen
        transportRecyclerView.layoutManager = GridLayoutManager(context,2) //second parameter specifies number of columns in grid

        //now we need to update the RecyclerView with each itemView and its corresponding data from the model layer
        updateUI()
        return view
    }

    //UPDATING THE FRAGMENT RecyclerView
    //----------------------------------
    private fun updateUI() {
        val transportList = transportListViewModel.transportList //grabs data from the model layer -> TransportListViewModel

        //Creating an adapter and connecting it to the model layer data
        adapter = TransportAdapter(transportList)
        //The adapter is responsible for...
        // --> creating the necessary ViewHolders and their itemViews when asked
        // --> binding ViewHolders to data from the model layer when asked

        //Connecting the RecyclerView to its adapter
        transportRecyclerView.adapter = adapter
        //The RecyclerView is responsible for...
        // -->  asking the adapter to create a new ViewHolder
        // -->  asking the adapter to bind a ViewHolder to an itemView using data from the model layer at a given position
    }

    //CREATING AND IMPLEMENTING A ViewHolder TO WRAP THE ITEM VIEWS FOR A RecyclerView
    //--------------------------------------------------------------------------------
    //A ViewHolder is responsible for...
    // --> wrapping an itemView and storing references to its layout View and inherent widgets
    // --> giving each itemView its attributes from the model layer data

    private inner class TransportHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener { //ViewHolder class will store the provided view in a property named itemView

        private lateinit var transport: Transport

        //referencing child views of the itemView using their view IDs (from list_item_transport.xml)
        private val transportNameTextView: TextView = itemView.findViewById(R.id.transport_name)
        private val transportImageView: ImageView = itemView.findViewById(R.id.transport_image)


        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //function used to tell the ViewHolder to bind to a Transport object
        fun bind(transport: Transport) {
            this.transport = transport

            //setting the text for the child text views of the itemView in the ViewHolder
            transportNameTextView.text = this.transport.name

            when (transport.name){
                "DDOT" -> {
                    transportImageView.setImageResource(R.drawable.ddot_bus)
                    transportNameTextView.setBackgroundResource(R.color.DdotGreen)
                }
                "People Mover" -> {
                    transportImageView.setImageResource(R.drawable.people_mover)
                    transportNameTextView.setBackgroundResource(R.color.PeopleMoverColor)
                }
                "QLine" -> {
                    transportImageView.setImageResource(R.drawable.qline)
                    transportNameTextView.setBackgroundResource(R.color.Qline)
                }
                "RefleX" -> {
                    transportImageView.setImageResource(R.drawable.reflex)
                    transportNameTextView.setBackgroundResource(R.color.ReflexBlue)
                }
                "SmartBus" -> {
                    transportImageView.setImageResource(R.drawable.smart)
                    transportNameTextView.setBackgroundResource(R.color.SmartBusRed)
                }
                "Route Map" -> {
                    transportImageView.setImageResource(R.drawable.route_map_card_image)
                    transportNameTextView.setBackgroundResource(R.color.PlanMyRoute)
                }
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${transport.name} pressed!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    //CREATING AN Adapter FOR THE RecyclerView
    //----------------------------------------
    private inner class TransportAdapter(var transportList: List<Transport>)//accepts and stores a list of Transport objects
        : RecyclerView.Adapter<TransportHolder>() {

        //creates a itemView, creates a viewHolder to wrap the itemView, and returns the viewHolder.
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TransportHolder {
            //inflates list_item_transport.xml and passes the resulting itemView to a new instance of TransportHolder
            val itemView = layoutInflater.inflate(R.layout.list_item_transport, parent, false)
            return TransportHolder(itemView)
        }

        //returns the number of items in the list of Transport objects
        override fun getItemCount() = transportList.size

        //binds the viewHolder with a Transport object from a given position in transportList
        override fun onBindViewHolder(holder: TransportHolder, position: Int) {
            val transport = transportList[position]
            holder.bind(transport)
        }
    }

    //FUNCTION THAT ACTIVITIES CAN CALL TO GET AN INSTANCE OF THE FRAGMENT
    //--------------------------------------------------------------------
    companion object {
        fun newInstance(): TransportListFragment {
            return TransportListFragment()
        }
    }



}


