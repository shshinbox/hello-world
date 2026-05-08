package me.helloworld.post

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {

    @PostMapping
    fun create(@RequestBody request: PostCreateRequest) = postService.create(request)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = postService.getPost(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: PostUpdateRequest) = postService.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = postService.delete(id)
}