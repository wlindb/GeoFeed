package se.umu.student.wili0037.geofeed.api

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.model.Posts

interface SimpleApi {

    @GET("post")
    suspend fun getPost(): Response<Post>

    @GET("posts/{cityName}")
    suspend fun getPosts(@Path("cityName") cityName: String): Response<Posts>

    @POST("post")
    suspend fun postNewPost(@Body post: Post): Response<Post>
}