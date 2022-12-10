package com.example.neighbor.models

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImplementation(val user: User) : UserDetails {
    private val expired = false
    private val locked = false
    private val enabled = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        arrayListOf(GrantedAuthority { user.role.name })

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.login

    override fun isAccountNonExpired(): Boolean = !expired

    override fun isAccountNonLocked(): Boolean = !locked

    override fun isCredentialsNonExpired(): Boolean = false

    override fun isEnabled(): Boolean = enabled
}