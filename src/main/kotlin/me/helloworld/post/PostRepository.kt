package me.helloworld.post

import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>