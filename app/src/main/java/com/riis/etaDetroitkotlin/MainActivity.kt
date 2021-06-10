package com.riis.etaDetroitkotlin


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.riis.etaDetroitkotlin.fragment.RouteMapFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    //global variables
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var drawerMenu: DrawerLayout

    //CREATING THE ACTIVITY
    //---------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        setNavigationMenu(navController)

    }

    //CLASS FUNCTIONS
    //---------------
    private fun setActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setNavigationMenu(navController: NavController) {
        val navView = findViewById<NavigationView>(R.id.side_nav_view)
        //if sideNavView exists, it uses the NavController to navigate to a destination when a menu item is selected from it
        navView?.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)
    }

    //HANDLING "UP" and DRAWER LAYOUT BEHAVIOUR IN THE ACTION BAR
    //===========================================================
    override fun onSupportNavigateUp(): Boolean {
        // tells the navController how to behave using the appBarConfiguration
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerMenu.closeDrawers()

        Handler(Looper.getMainLooper()).postDelayed({
            when (item.itemId) {
                R.id.nav_route_map -> navController.navigate(R.id.action_home_dest_to_routeMapFragment)
                R.id.nav_ddot -> navController.navigate(R.id.moveToRoutesFragment)
                R.id.nav_smart -> navController.navigate(R.id.moveToRoutesFragment)
                R.id.nav_reflex -> navController.navigate(R.id.moveToRoutesFragment)
                R.id.nav_people_mover -> navController.navigate(R.id.moveToRoutesFragment)
                R.id.nav_qline -> navController.navigate(R.id.moveToRoutesFragment)
            }
        }, 1000)

        return true
    }


}