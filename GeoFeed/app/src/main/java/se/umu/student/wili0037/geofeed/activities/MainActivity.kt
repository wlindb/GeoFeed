package se.umu.student.wili0037.geofeed.activities

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.activities.adapters.RecyclerAdapter
import se.umu.student.wili0037.geofeed.fragments.MainFragment
import se.umu.student.wili0037.geofeed.repository.Repository



class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.geocoder = Geocoder(this)
        viewModel.initLocationSubscription()
        viewModel.cityName.observe(this, Observer { cityName ->
            updateSubTitle(cityName)
        })
        navController = findNavController(R.id.fragment)
        setupActionBarWithNavController(navController)
    }


    private fun updateSubTitle(cityName: String) {
        val actionBar = supportActionBar
        actionBar?.subtitle = cityName
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationSubscription()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLocationSubscription(this, this)
    }

}
