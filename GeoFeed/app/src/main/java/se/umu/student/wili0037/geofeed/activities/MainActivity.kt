package se.umu.student.wili0037.geofeed.activities

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import se.umu.student.wili0037.geofeed.MainViewModel
import se.umu.student.wili0037.geofeed.MainViewModelFactory
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.activities.adapters.RecyclerAdapter
import se.umu.student.wili0037.geofeed.repository.Repository
import java.sql.Timestamp
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        //viewModel.getPosts()

        viewModel.responsePosts.observe(this, Observer { response ->
            if(response.isSuccessful) {
                if (response.body() == null) return@Observer
                val rv_recyclerView: RecyclerView = findViewById(R.id.rv_recyclerView) as RecyclerView
                rv_recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RecyclerAdapter(response.body()!!.posts)
                }
            } else {
                Log.d("Response", response.code().toString())
            }
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this)
        initLocationSubscription()
    }

    private fun initLocationSubscription() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000*5 // every 5 seconds
            fastestInterval = 1000*5
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime= 1000*6
            smallestDisplacement = 3000f // User has to move 3km before update
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.d("getLocationUpdates", "onLocationResult: inne")
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    Log.d("getLocationUpdates", "onLocationResult: ${location.latitude}, ${location.longitude}")
                    updateSubTitle(getCityName(location))
                }


            }
        }
    }

    private fun getCityName(location: Location): String {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 10)
        val address = addresses.find { address -> address.locality != null }
        // ===========================
        // Move this to other function
        if ( address != null) {
            viewModel.getPosts()
        }
        // ===========================
        return if(address != null) {
            address.locality
        } else {
            "Unknown City"
        }
    }

    private fun updateSubTitle(cityName: String) {
        val actionBar = supportActionBar
        actionBar?.subtitle = cityName
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startLocationSubscription() {
        // If location permissions not given, ask user for permission
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun stopLocationSubscription() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationSubscription()
    }

    override fun onResume() {
        super.onResume()
        startLocationSubscription()
    }
}
