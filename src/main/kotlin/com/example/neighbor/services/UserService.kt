package com.example.neighbor.services

import com.example.neighbor.dto.SecurityTokenDto
import com.example.neighbor.dto.UserAuthDto
import com.example.neighbor.models.User
import com.example.neighbor.repostories.UserRepository
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.JsonPatchException
import org.springframework.http.HttpStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors
import javax.persistence.EntityExistsException


@Service
class UserService(val repository: UserRepository, val encoder: JwtEncoder, val userDetailsService: UserDetailsService) {
    fun getByLogin(login: String): User {
        return repository.findByLogin(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun createUser(user: User): User {
        if (repository.findByLogin(user.login) != null) {
            throw EntityExistsException("User already exists")
        }

        return repository.save(user)
    }

    fun getToken(userAuthDto: UserAuthDto): SecurityTokenDto {
        val details: UserDetails = userDetailsService.loadUserByUsername(userAuthDto.login)

        val now = Instant.now()
        val expiry = 5000L
        val expiredAt = now.plusSeconds(expiry)
        val scope = details.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(" "))

        val claims: JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(expiredAt)
            .subject(details.username)
            .claim("scope", scope).build()

        val token: String = this.encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
        val localExpiredAt = LocalDateTime.ofInstant(expiredAt, ZoneId.systemDefault())
        return SecurityTokenDto(details.username, scope, token, localExpiredAt)
    }

    fun getAllUsers(): List<User> {
        return repository.findAll().toList()
    }

    fun getUser(login: String): User? {
        return repository.findByLogin(login)
    }

    fun updateUser(newUser: User): User {
        return repository.save(newUser)
    }

    fun applyPatchToUser(user: User, patch: JsonPatch): User? {
        val mapper = ObjectMapper()
        val userJson = mapper.valueToTree<JsonNode>(user)
        return try {
            mapper.treeToValue(patch.apply(userJson), User::class.java)
        } catch (e: JsonPatchException) {
            null
        } catch (e: JsonProcessingException) {
            null
        }
    }
}