package ru.neighbor.models

import javax.persistence.*

@Entity
@Table(name = "users")
class User(val login: String, val email: String, val phone: String, val name: String, val password: String) {
    @Id
    @GeneratedValue
    val id: Long = 0

    @OneToOne
    @JoinColumn
    var avatar: Image? = null
    val description: String? = null
    val role: Role = Role.User
}