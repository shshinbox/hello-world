package me.helloworld.common

data class PostCreateRequest(val title: String, val content: String)
data class PostUpdateRequest(val title: String, val content: String)
data class PostResponse(val id: Long, val title: String, val content: String)