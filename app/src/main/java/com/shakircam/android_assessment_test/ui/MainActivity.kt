package com.shakircam.android_assessment_test.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.shakircam.android_assessment_test.R
import com.shakircam.android_assessment_test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        //bottom nav
        binding.bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.repositoryFragment,
                R.id.historyFragment
            ), binding.drawerLayout
        )

        //menu item click handle
        binding.navigationView.setupWithNavController(navController)
        // connect appbar with nav controller
        setupActionBarWithNavController(navController, appBarConfiguration)

        //hide bottom navigation in specific fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                    R.id.repositoryDetailsFragment ->
                    binding.bottomNavigationView.visibility = View.GONE

                else ->
                    binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    //open drawer when drawer icon clicked and back btn pressed
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

}