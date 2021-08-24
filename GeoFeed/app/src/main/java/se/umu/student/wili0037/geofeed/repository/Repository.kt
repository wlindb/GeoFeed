package se.umu.student.wili0037.geofeed.repository

import retrofit2.Response
import se.umu.student.wili0037.geofeed.api.RetrofitInstance
import se.umu.student.wili0037.geofeed.model.Comment
import se.umu.student.wili0037.geofeed.model.Post
import se.umu.student.wili0037.geofeed.model.Posts

class Repository {

    suspend fun getPosts(cityName: String): Response<Posts> {
        return RetrofitInstance.api.getPosts(cityName)
    }

    suspend fun postNewPost(post: Post): Response<Post> {
        return RetrofitInstance.api.postNewPost(post)
    }

    suspend fun postComment(_id: String, comment: Comment): Response<Post> {
        return RetrofitInstance.api.postComment(_id, comment)
    }

    suspend fun getPostsByUUID(uuid: String): Response<Posts> {
        return RetrofitInstance.api.getPostsByUUID(uuid)
    }
}