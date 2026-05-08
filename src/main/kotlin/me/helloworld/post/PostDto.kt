package me.helloworld.post

data class PostCreateRequest(val title: String, val content: String)
data class PostUpdateRequest(val title: String, val content: String)
data class PostResponse(val id: Long, val title: String, val content: String)