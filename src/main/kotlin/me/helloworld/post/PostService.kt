package me.helloworld.post

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(private val postRepository: PostRepository) {

    @Transactional
    fun create(request: PostCreateRequest): Long {
        val post = postRepository.save(Post(title = request.title, content = request.content))
        return post.id!!
    }

    fun getPost(id: Long): PostResponse {
        val post = postRepository.findById(id).orElseThrow { RuntimeException("Post not found") }
        return PostResponse(post.id!!, post.title, post.content)
    }

    @Transactional
    fun update(id: Long, request: PostUpdateRequest) {
        val post = postRepository.findById(id).orElseThrow { RuntimeException("Post not found") }
        post.title = request.title
        post.content = request.content
    }

    @Transactional
    fun delete(id: Long) = postRepository.deleteById(id)
}