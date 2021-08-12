package se.umu.student.wili0037.geofeed.repository

import se.umu.student.wili0037.geofeed.api.RetrofitInstance
import se.umu.student.wili0037.geofeed.model.Post

class Repository {

    suspend fun getPost(): Post {
        return RetrofitInstance.api.getPost()
    }
}