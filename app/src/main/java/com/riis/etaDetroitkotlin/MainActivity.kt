package com.riis.etaDetroitkotlin


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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


class MainActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener{

    //global variables
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerMenu: DrawerLayout
    private lateinit var listOfCompanies: List<Company>

    private val sharedViewModel: SharedViewModel by viewModels()

    //CREATING THE ACTIVITY
    //---------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        sharedViewModel.companyListLiveData.observe(
            this,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )

        setContentView(R.layout.activity_main) //inflating view from activity_main layout

        //defining the navigation host and getting access to its navigation controller
        val navHost: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = navHost.navController

        //creating an app bar
        val appBar = findViewById<Toolbar>(R.id.app_bar)
        setSupportActionBar(appBar) //setting the ToolBar as the activity's action bar

        //defining app bar configurations
        drawerMenu = findViewById(R.id.drawer_menu)
        appBarConfig = AppBarConfiguration(
            setOf(R.id.home_dest), //setting the top-level fragment destinations
            drawerMenu
        ) //giving the app bar a drawerLayout

        //setting the app bar and side navigation view to work with the Navigation jetpack architecture
        setActionBar(navController, appBarConfig)

        setupNavigationMenu(navController)

    }


    //CLASS FUNCTIONS
    //---------------
    private fun setActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }


    //HANDLING "UP" and DRAWER LAYOUT BEHAVIOUR IN THE ACTION BAR
    //===========================================================
    override fun onSupportNavigateUp(): Boolean {
        // tells the navController how to behave using the appBarConfiguration
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.side_nav_view) //referencing the view
        sideNavView?.setupWithNavController(navController)//if sideNavView exists, it can use the NavController to navigate when a menu item is selected
        sideNavView?.setNavigationItemSelectedListener(this)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerMenu.closeDrawers()

        if (item.groupId == R.id.primary){
            return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                    || super.onOptionsItemSelected(item) // Otherwise, returns false
        }
        else{
            Handler(Looper.getMainLooper()).postDelayed({

                when (item.title) {

                    this.getString(R.string.navDdot) ->{
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
            }, 500)
        }


        return true

    }



}