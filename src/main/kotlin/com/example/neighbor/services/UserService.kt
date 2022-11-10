package com.example.neighbor.services

import com.example.neighbor.models.User
import com.example.neighbor.repostories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(val repository: UserRepository) {
    fun getByLogin(login: String): User {
        return repository.findByLogin(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}