package me.helloworld.reactive

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository("reactivePostRepository")
interface PostRepository : CoroutineCrudRepository<Post, Long>
