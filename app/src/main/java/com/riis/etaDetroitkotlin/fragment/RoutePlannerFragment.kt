package com.riis.etaDetroitkotlin.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.model.Company

class RoutePlannerFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var listOfCompanies: List<Company>
    private lateinit var navController: NavController
    private lateinit var drawerMenu: DrawerLayout
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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

        navController = view.findNavController()
        drawerMenu = requireActivity().findViewById(R.id.drawer_menu)
        val navView = activity?.findViewById<NavigationView>(R.id.side_nav_view)
        navView?.setupWithNavController(navController)
        navView?.setNavigationItemSelectedListener(this)

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerMenu.closeDrawers()

        Handler(Looper.getMainLooper()).postDelayed({
            when (item.itemId) {

                R.id.nav_home -> navController.navigate(R.id.routePlannerFragment_to_homeFragment)

                R.id.nav_route_map -> navController.navigate(R.id.routePlannerFragment_to_routeMapFragment)

                R.id.nav_ddot -> {
                    sharedViewModel.saveCompany(listOfCompanies[1])
                    navController.navigate(R.id.routePlannerFragment_to_routesFragment)
                }
                R.id.nav_smart -> {
                    sharedViewModel.saveCompany(listOfCompanies[0])
                    navController.navigate(R.id.routePlannerFragment_to_routesFragment)
                }
                R.id.nav_reflex -> {
                    sharedViewModel.saveCompany(listOfCompanies[2])
                    navController.navigate(R.id.routePlannerFragment_to_routesFragment)
                }
                R.id.nav_people_mover -> {
                    sharedViewModel.saveCompany(listOfCompanies[3])
                    navController.navigate(R.id.routePlannerFragment_to_routesFragment)
                }
                R.id.nav_qline -> {
                    sharedViewModel.saveCompany(listOfCompanies[4])
                    navController.navigate(R.id.routePlannerFragment_to_routesFragment)
                }
                R.id.nav_planner -> {
                    navController.navigate(R.id.routePlannerFragment_to_routePlannerFragment)
                }
            }
        }, 500)

        return true
    }
}