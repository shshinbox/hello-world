package me.helloworld.mvc

import me.helloworld.common.PostCreateRequest
import me.helloworld.common.PostResponse
import me.helloworld.common.PostUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(transactionManager = "transactionManager", readOnly = true)
class PostService(private val postRepository: PostRepository) {

    @Transactional(transactionManager = "transactionManager")
    fun create(request: PostCreateRequest): Long {
        val post = postRepository.save(Post(title = request.title, content = request.content))
        return post.id!!
    }

    fun getPost(id: Long): PostResponse {
        val post = postRepository.findById(id).orElseThrow { RuntimeException("Post not found") }
        return PostResponse(post.id!!, post.title, post.content)
    }

    @Transactional(transactionManager = "transactionManager")
    fun update(id: Long, request: PostUpdateRequest) {
        val post = postRepository.findById(id).orElseThrow { RuntimeException("Post not found") }
        post.title = request.title
        post.content = request.content
    }

    @Transactional(transactionManager = "transactionManager")
    fun delete(id: Long) = postRepository.deleteById(id)
}
