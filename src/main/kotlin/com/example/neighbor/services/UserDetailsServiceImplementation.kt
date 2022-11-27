package com.example.neighbor.services

import com.example.neighbor.models.UserDetailsImplementation
import com.example.neighbor.repostories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImplementation(private val repository: UserRepository) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(login: String): UserDetails {
        val user = repository.findByLogin(login) ?: throw UsernameNotFoundException("username $login")
        return UserDetailsImplementation(user)
    }
}