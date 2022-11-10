package com.example.neighbor.models

import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(@Id @GeneratedValue val id: Long, val login: String, val name: String) {
}