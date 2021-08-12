package se.umu.student.wili0037.geofeed.api

import retrofit2.http.GET
import se.umu.student.wili0037.geofeed.model.Post

interface SimpleApi {

    @GET("post")
    suspend fun getPost(): Post
}