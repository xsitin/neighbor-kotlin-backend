package com.example.neighbor.models

import javax.persistence.*


@Entity
@Table(name = "ads")
class Advertisement(val title: String, val description: String, val category: String, val price: Int = 0) {
    @Id
    @GeneratedValue
    val id: Long = 0

    @ManyToOne
    @JoinColumn
    lateinit var owner: User

    @ManyToMany
    @JoinColumn
    lateinit var images: List<Image>
}