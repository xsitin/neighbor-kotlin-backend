package ru.neighbor.controllers

import com.github.fge.jsonpatch.JsonPatch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import ru.neighbor.dto.*
import ru.neighbor.infrastructure.mappers.UserMapper
import ru.neighbor.models.Image
import ru.neighbor.models.Role
import ru.neighbor.models.User
import ru.neighbor.services.ImageService
import ru.neighbor.services.UserService
import java.io.IOException


@RestController
@CrossOrigin
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

    @GetMapping("{login}/full")
    fun getFullUserInfo(@PathVariable login: String, auth: Authentication): UserFullDto {
        val role = if (auth.authorities.isEmpty()) "" else auth.authorities.stream().findFirst().get().authority
        if (!(auth.name == login || role == Role.Administrator.name)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
        val user = userService.getUser(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")

        return mapper.userToUserFullDto(user)
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
