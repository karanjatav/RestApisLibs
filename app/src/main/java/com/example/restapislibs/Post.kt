package com.example.restapislibs

data class Post(
    var userId: Int = 0,
    var id: Int? = null,
    var title: String? = null,
    var body: String? = null
)