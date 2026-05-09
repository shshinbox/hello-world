package me.helloworld.sample

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/helloworld")
class HelloWorldController {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): String = "Hello World! $id"
}