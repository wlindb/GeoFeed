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
        /*
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
        */
        viewModel.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.geocoder = Geocoder(this)
        viewModel.initLocationSubscription()
        viewModel.cityName.observe(this, Observer { cityName ->
            updateSubTitle(cityName)
        })
        navController = findNavController(R.id.fragment)
        setupActionBarWithNavController(navController)
        /*
        val fab: View = findViewById(R.id.floating_action_button)
        fab.setOnClickListener { view ->
            intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
        */

        /*
        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, mainFragment)
            commit()
        }
        */
    }


    private fun updateSubTitle(cityName: String) {
        val actionBar = supportActionBar
        actionBar?.subtitle = cityName
    }

/*
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

 */


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
