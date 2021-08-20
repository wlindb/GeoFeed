package se.umu.student.wili0037.geofeed

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.provider.Settings.Secure
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.launch
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository
import retrofit2.Response
import se.umu.student.wili0037.geofeed.activities.MainActivity
import se.umu.student.wili0037.geofeed.model.Comment
import se.umu.student.wili0037.geofeed.model.Posts
import java.time.LocalDate
import java.util.*

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    val responsePosts: MutableLiveData<Response<Posts>> = MutableLiveData()
    val responseUserPosts: MutableLiveData<Response<Posts>> = MutableLiveData()
    var responseNewPost: MutableLiveData<Response<Post>> = MutableLiveData()
    val cityName: MutableLiveData<String> = MutableLiveData("Unknown city")
    val district: MutableLiveData<String> = MutableLiveData("Unknown district")
    val currentPost: MutableLiveData<Post> = MutableLiveData()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var geocoder: Geocoder


    fun getPost() {
        viewModelScope.launch {
            val response = repository.getPost()
            myResponse.value = response
        }
    }

    fun createNewPost(uuid: String, body: String) {
        if (cityName.value != null) {
            //val timestamp = Calendar.getInstance().time.toString()
            val timestamp = System.currentTimeMillis().toString()
            val comment = listOf<Comment>()
            val newPost = Post(null, uuid, cityName.value!!, district.value.toString(), body, timestamp, comment)
            Log.d("Response", "createNewPost: ${newPost.toString()}")
            // Handle Post
            viewModelScope.launch {
                val response = repository.postNewPost(newPost)
                responseNewPost.value = response

            }
        }
    }

    fun getPosts() {
        if (cityName.value.equals("Unknown city") || cityName.value == null) return
        viewModelScope.launch {
            Log.d("Respone", "getPosts: ${cityName.value}")
            val response = repository.getPosts(cityName.value!!)
            responsePosts.value = response
        }
    }

    private fun getPosts(city: String) {
        viewModelScope.launch {
            Log.d("Respone", "getPosts: ${cityName.value}")
            val response = repository.getPosts(city)
            responsePosts.value = response
        }
    }

    fun clearResponseNewPost() {
        responseNewPost = MutableLiveData()
    }

    fun initLocationSubscription() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000*5 // every 5 seconds
            fastestInterval = 1000*5
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime= 1000*6
            smallestDisplacement = 3000f // User has to move 3km before update
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    handleLocationUpdate(location)
                }
            }
        }
    }

    private fun handleLocationUpdate(location: Location) {
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 10)
        val address = addresses.find { address -> address.locality != null } ?: return
        if (address.locality != cityName.value) {
            Log.d("Respone", "setCityNameFromLocation: ${address.toString()}, ${cityName.value}")
            cityName.value = address.locality
            district.value = address.featureName?.toString()
            getPosts(address.locality)
        }
    }

    fun startLocationSubscription(context: Context, mainActivity: MainActivity) {
        // If location permissions not given, ask user for permission
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    fun stopLocationSubscription() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun setCurrentPost(post: Post) {
        currentPost.value = post
    }

    fun postComment(_id: String, uuid: String, commentText: String) {
        Log.d("Response", "postComment: innan req")
        if(cityName.value != null && district.value != null) {
            val timestamp = System.currentTimeMillis().toString()
            val newComment = Comment(uuid, commentText, timestamp, cityName.value.toString(), district.value.toString())
            viewModelScope.launch {
                val response = repository.postComment(_id, newComment)
                if (response.isSuccessful && response.body() != null) {
                    val post: Post = response.body()!!
                    Log.d("Response", "postComment: $post")
                    currentPost.value = post
                }
            }
        }
    }

    fun getPostsByUUID(uuid: String) {
        viewModelScope.launch {
            val response = repository.getPostsByUUID(uuid)
            responseUserPosts.value = response
            //responsePosts.value = response
        }
    }
}