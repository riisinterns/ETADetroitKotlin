package com.riis.etaDetroitkotlin


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.riis.etaDetroitkotlin.model.Company


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //GLOBAL CLASS VARIABLES
    //----------------------
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerMenu: DrawerLayout
    private lateinit var listOfCompanies: List<Company>

    //links the activity to a viewModel shared with the all of the fragments
    private val sharedViewModel: SharedViewModel by viewModels()

    //CREATING THE ACTIVITY
    //---------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //observing live data from the sharedViewModel which returns a list of Company objects
        sharedViewModel.companyListLiveData.observe(
            this,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList //updating the class's list of Company objects
                }
            }
        )

        setContentView(R.layout.activity_main) //inflating the activity's view from activity_main layout

        //defining the navigation host and getting access to its navigation controller
        val navHost: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = navHost.navController

        //creating an app bar from the toolbar defined in the inflated layout
        val appBar = findViewById<Toolbar>(R.id.app_bar)
        setSupportActionBar(appBar) //setting the ToolBar as the activity's action bar (can now host a menu and navigation button)

        drawerMenu = findViewById(R.id.drawer_menu)

        //defining configurations for the app bar
        appBarConfig = AppBarConfiguration(
            setOf(R.id.home_dest), //setting the top-level fragment destinations in nav graph
            drawerMenu //giving the app bar access to a drawerLayout
        )

        //enabling the app bar and side navigation view to work with the nav graph
        setActionBar(navController, appBarConfig)
        setupNavigationMenu(navController)

    }


    //ACTION BAR AND SIDE NAVIGATION SETUP FUNCTIONS
    //----------------------------------------------
    private fun setActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.side_nav_view) //referencing the view
        sideNavView?.setupWithNavController(navController)//if sideNavView exists, it can use the NavController to navigate when a menu item is selected
        sideNavView?.setNavigationItemSelectedListener(this)
    }


    //HANDLING NAVIGATION USING "UP" BUTTON IN ACTION BAR
    //---------------------------------------------------
    //This method is called whenever the user chooses to navigate Up within your application's activity hierarchy from the action bar.
    override fun onSupportNavigateUp(): Boolean {
        // tells the navController how to navigate based on the state of the app bar
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    //HANDLING NAVIGATION WHEN AN ITEM IS SELECTED FROM THE SIDE NAVIGATION MENU (drawer_menu.xml)
    //--------------------------------------------------------------------------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerMenu.closeDrawers() //closes the side nav

        //If the selected menu item is part of the "primary group" in the drawer menu layout ...
        if (item.groupId == R.id.primary) {
            // navigate to the fragment destination whose id matches the id of the selected menu item
            return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                    || super.onOptionsItemSelected(item) // Otherwise, returns false
        } else {
            //The remaining menu items all navigate to the RoutesFragment. Based on which is selected, the data ...
            // ... saved to the shared view model is different and the fragment's UI changes accordingly

            when (item.title) {

                this.getString(R.string.navDdot) -> {
                    sharedViewModel.saveCompany(listOfCompanies[1])
                    navController.navigate(R.id.routes_dest, null, null)
                }
                this.getString(R.string.navSmart) -> {
                    sharedViewModel.saveCompany(listOfCompanies[0])
                    navController.navigate(R.id.routes_dest, null, null)
                }
                this.getString(R.string.navReflex) -> {
                    sharedViewModel.saveCompany(listOfCompanies[2])
                    navController.navigate(R.id.routes_dest, null, null)
                }
                this.getString(R.string.navPeopleMover) -> {
                    sharedViewModel.saveCompany(listOfCompanies[3])
                    navController.navigate(R.id.routes_dest, null, null)
                }
                this.getString(R.string.navQline) -> {
                    sharedViewModel.saveCompany(listOfCompanies[4])
                    navController.navigate(R.id.routes_dest, null, null)
                }
            }

        }

        return true
    }

}