package se.umu.student.wili0037.geofeed.repository

import retrofit2.Response
import se.umu.student.wili0037.geofeed.api.RetrofitInstance
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.model.Posts

class Repository {

    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }

    suspend fun getPosts(cityName: String): Response<Posts> {
        return RetrofitInstance.api.getPosts(cityName)
    }
}