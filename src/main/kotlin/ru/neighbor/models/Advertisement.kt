package ru.neighbor.models

import javax.persistence.*


@Entity
@Table(name = "ads")
class Advertisement(
    @Id
    @GeneratedValue
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Int = 0,
    var rating: Int = 0
) {
    @ManyToOne
    @JoinColumn
    lateinit var owner: User

    @ManyToMany
    @JoinColumn
    lateinit var images: List<Image>
}