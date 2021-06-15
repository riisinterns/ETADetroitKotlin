package com.riis.etaDetroitkotlin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.Company


//HomeFragment is a fragment that displays a grid-based RecyclerView
//It is the first screen the user sees and provides the interface for the user to select between different bus companies

class HomeFragment : Fragment() {

    //GLOBAL CLASS VARIABLES
    //----------------------
    private lateinit var companyRecyclerView: RecyclerView


    //links the fragment to a viewModel shared with MainActivity and other fragments
    private val sharedViewModel: SharedViewModel by activityViewModels()

    //INFLATING THE FRAGMENT VIEW FROM A LAYOUT
    //-----------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //inflating the fragment_home layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //RecyclerView setup (Grid Layout)
        companyRecyclerView = view.findViewById(R.id.company_recycler_view) as RecyclerView
        companyRecyclerView.layoutManager =
            GridLayoutManager(context, 2) //second parameter specifies number of columns in grid

        return view
    }

    //UPDATING THE UI BASED ON NEW MODEL DATA
    //---------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    updateUI(companyList)
                }
            }
        )
    }


    //FUNCTION TO UPDATE UI
    //---------------------
    private fun updateUI(companies: List<Company>) {
        //Connecting the RecyclerView to a new adapter populated with updated model data

        postponeEnterTransition()
        view?.doOnPreDraw { startPostponedEnterTransition() }

        companyRecyclerView.adapter = CompanyAdapter(companies)
    }

    //VIEW HOLDER CLASS FOR RECYCLER VIEW
    //-----------------------------------
    private inner class CompanyHolder(view: View)
    //When given a view, it is used as a reusable blueprint for creating itemViews
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var companyItem: Company //instantiating a new Company object to later receive model layer data

        //referencing the itemView's child views from the list_item_transport layout
        private val companyNameTextView: TextView = itemView.findViewById(R.id.company_name)
        private val companyImageView: ImageView = itemView.findViewById(R.id.busImgView)

        init {
            itemView.setOnClickListener(this) //setting a click listener on each itemView
        }

        //binding the viewHolder's Company object to date of another from the model layer
        fun bind(company: Company) {
            companyItem = company

            //updating the itemView's title, background color, and image using the received data
            companyNameTextView.text = companyItem.name

            companyNameTextView.setBackgroundColor(Color.parseColor(companyItem.brandColor))

            val resID: Int = requireContext().resources.getIdentifier(
                companyItem.busImgUrl,
                "drawable",
                context!!.packageName
            )
            companyImageView.setImageResource(resID)

            ViewCompat.setTransitionName(companyImageView, companyItem.id.toString())

        }

        //HANDLING NAVIGATION WHEN A ITEM VIEW (Company) IS SELECTED FROM RECYCLER VIEW
        //-----------------------------------------------------------------------------
        override fun onClick(itemView: View) {
            if (companyItem.name == "Route Map") {
                findNavController().navigate(R.id.action_home_dest_to_routeMapFragment) //navigate to RouteMapFragment
            } else {
                //save the selected itemView's Company object to the shared view model and navigate to the RoutesFragment
                sharedViewModel.saveCompany(companyItem)

                val extras = FragmentNavigatorExtras(
                    companyImageView to "bus_transition"
                )
                itemView.findNavController().navigate(R.id.moveToRoutesFragment, null, null, extras)
            }
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

}


