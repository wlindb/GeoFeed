package se.umu.student.wili0037.geofeed.model

import java.sql.Timestamp

data class Post (
    val uuid: String,
    val location: String,
    val district: String,
    val body: String,
    val timestamp: Timestamp,
    val comments: List<String>
)