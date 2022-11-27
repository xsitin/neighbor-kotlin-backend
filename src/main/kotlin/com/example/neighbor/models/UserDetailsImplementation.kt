package com.example.neighbor.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImplementation(val user: User) : UserDetails {
    private val expired = false
    private val locked = false
    private val enabled = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return arrayListOf(GrantedAuthority { user.role.name })
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.name
    }

    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}