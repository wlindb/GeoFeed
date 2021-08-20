package se.umu.student.wili0037.geofeed.model

data class Comment(
    val uuid: String,
    val comment: String,
    val timestamp: String,
    val location: String,
    val district: String,
)
