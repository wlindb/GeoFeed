package se.umu.student.wili0037.geofeed.api

import retrofit2.http.GET
import retrofit2.Response
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.model.Posts

interface SimpleApi {

    @GET("post")
    suspend fun getPost(): Response<Post>

    @GET("posts")
    suspend fun getPosts(): Response<Posts>
}