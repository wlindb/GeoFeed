package se.umu.student.wili0037.geofeed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.repository.Repository
import retrofit2.Response
import se.umu.student.wili0037.geofeed.model.Posts

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    val responsePosts: MutableLiveData<Response<Posts>> = MutableLiveData()

    fun getPost() {
        viewModelScope.launch {
            val response = repository.getPost()
            myResponse.value = response
        }
    }

    fun getPosts() {
        viewModelScope.launch {
            val response = repository.getPosts()
            responsePosts.value = response
        }
    }
}