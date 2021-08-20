package se.umu.student.wili0037.geofeed.model

data class Post (
    val _id: String?,
    val uuid: String,
    val location: String,
    val district: String,
    val body: String,
    val timestamp: String,
    val comments: List<Comment>
)