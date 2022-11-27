package com.example.neighbor.controllers

import com.example.neighbor.dto.SecurityTokenDto
import com.example.neighbor.dto.UserAuthDto
import com.example.neighbor.dto.UserPublicDto
import com.example.neighbor.dto.UserRegistrationDto
import com.example.neighbor.infrastructure.mappers.UserMapper
import com.example.neighbor.models.Image
import com.example.neighbor.models.User
import com.example.neighbor.services.ImageService
import com.example.neighbor.services.UserService
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException

@RestController
@RequestMapping("accounts")
class AccountController(
    val userService: UserService,
    val imageService: ImageService,
    @Qualifier("userMapperImpl") val mapper: UserMapper
) {
    @GetMapping("my-name")
    fun getMyName(auth: Authentication): String? {
        return auth.name
    }

    @GetMapping("users")
    @ResponseBody
    fun getAllUsers(): List<User?> {
        return userService.getAllUsers()
    }

    @PostMapping("login", consumes = ["application/json"])
    fun login(@RequestBody userAuthDto: UserAuthDto): SecurityTokenDto? {
        return userService.getToken(userAuthDto)
    }

    @GetMapping("{login}")
    @ResponseBody
    fun getPublicUserInfo(@PathVariable login: String): UserPublicDto {
        val user = userService.getUser(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        return mapper.userToUserPublicDto(user)
    }

    @PatchMapping(value = ["{login}"], consumes = ["application/json-patch+json"])
    @ResponseBody
    fun updateUserInfo(auth: Authentication, @RequestBody patch: JsonPatch): UserPublicDto {
        val user: User = userService.getUser(auth.name)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        val userPatched = userService.applyPatchToUser(user, patch)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "unable to apply patch")
        val newUser = userService.updateUser(userPatched)
        return mapper.userToUserPublicDto(newUser)
    }

    @PostMapping("registration")
    fun registerUser(
        @RequestPart("AccountRegistration") userRegisterDTO: UserRegistrationDto,
        @RequestPart("avatar") file: MultipartFile
    ): SecurityTokenDto {
        val image: Image = try {
            imageService.createImage(file.bytes)
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "incorrect avatar", e)
        }

        var user = mapper.userRegisterDtoToUser(userRegisterDTO)
        user.avatar = image
        user = userService.createUser(user)
        return userService.getToken(mapper.userToUserAuthDto(user))
    }
}
