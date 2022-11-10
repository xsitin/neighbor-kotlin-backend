package com.example.neighbor.controllers

import com.example.neighbor.dto.UserPublicDto
import com.example.neighbor.infrastructure.mappers.UserMapper
import com.example.neighbor.models.HelloObj
import com.example.neighbor.models.User
import com.example.neighbor.services.UserService
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("accounts")
class AccountController(val service: UserService, @Qualifier("userMapperImpl") val mapper: UserMapper) {

    @GetMapping("/hello/{name}")
    fun hello(@PathVariable name: String): HelloObj {
        return HelloObj(name)
    }

    @GetMapping("{login}")
    fun getPublicInfo(@PathVariable login: String): UserPublicDto {
        return mapper.userToUserPublicDto(service.getByLogin(login))
    }

}
