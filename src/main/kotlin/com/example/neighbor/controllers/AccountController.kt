package com.example.neighbor.controllers

import com.example.neighbor.models.HelloObj
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("accounts")
class AccountController {

    @GetMapping("/hello/{name}")
    fun hello(@PathVariable name: String): HelloObj {
        return HelloObj(name)
    }

}
