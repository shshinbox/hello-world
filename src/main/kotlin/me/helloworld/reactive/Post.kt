package me.helloworld.reactive

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("post")
class Post(
    @Id
    var id: Long? = null,

    var title: String,

    var content: String,
)
