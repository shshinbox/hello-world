package me.helloworld.reactive

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.*
import me.helloworld.common.PostCreateRequest
import me.helloworld.common.PostResponse
import me.helloworld.common.PostUpdateRequest


@RestController("reactivePostController")
@RequestMapping("/api/reactive/posts")
class PostController(private val postService: PostService) {

    @PostMapping
    suspend fun create(@RequestBody request: PostCreateRequest): Long =
        postService.create(request)

    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: Long): PostResponse =
        postService.getPost(id)

    @GetMapping
    fun getAll(): Flow<PostResponse> =
        postService.getAllPosts()

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: Long, @RequestBody request: PostUpdateRequest) =
        postService.update(id, request)

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: Long) =
        postService.delete(id)
}