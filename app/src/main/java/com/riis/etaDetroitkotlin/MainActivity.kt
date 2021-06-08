package com.riis.etaDetroitkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    //creating a new activity using whatever data is stored in savedInstanceState
    //...and setting the content to be displayed in activity_main.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //asking the FragmentManager for the current fragment being used in fragment_container from activity_main.xml
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        //if there is no current fragment, create a fragment from CompanyListFragment.kt
        if (currentFragment == null) {
            val fragment = CompanyListFragment.newInstance()

            //Create a new fragment transaction, include one add operation in it, and then commit it
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)//adds the fragment to the container
                .commit()
        }
    }
}