package com.riis.etaDetroitkotlin


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {

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


}