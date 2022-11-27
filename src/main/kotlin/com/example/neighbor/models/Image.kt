package com.example.neighbor.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "images")
class Image(val data: ByteArray) {
    @Id
    @GeneratedValue
    val id: Long = 0
}