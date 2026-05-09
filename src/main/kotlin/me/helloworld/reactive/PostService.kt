package me.helloworld.reactive

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.helloworld.common.PostCreateRequest
import me.helloworld.common.PostResponse
import me.helloworld.common.PostUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("reactivePostService")
@Transactional(transactionManager = "connectionFactoryTransactionManager", readOnly = true)
class PostService(private val postRepository: PostRepository) {

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    suspend fun create(request: PostCreateRequest): Long {
        val post = postRepository.save(Post(title = request.title, content = request.content))
        return post.id ?: throw IllegalStateException("Post id was not generated")
    }

    suspend fun getPost(id: Long): PostResponse {
        val post = postRepository.findById(id) ?: throw RuntimeException("Post not found")
        return post.toResponse()
    }

    fun getAllPosts(): Flow<PostResponse> {
        return postRepository.findAll()
            .map { it.toResponse() }
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    suspend fun update(id: Long, request: PostUpdateRequest) {
        val post = postRepository.findById(id) ?: throw RuntimeException("Post not found")
        post.title = request.title
        post.content = request.content
        postRepository.save(post)
    }

    @Transactional(transactionManager = "connectionFactoryTransactionManager")
    suspend fun delete(id: Long) {
        postRepository.deleteById(id)
    }

    private fun Post.toResponse(): PostResponse {
        return PostResponse(
            id = id ?: throw IllegalStateException("Post id was not generated"),
            title = title,
            content = content,
        )
    }
}
