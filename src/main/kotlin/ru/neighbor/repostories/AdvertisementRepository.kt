package ru.neighbor.repostories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.neighbor.models.Advertisement
import java.util.*

@Repository
interface AdvertisementRepository : JpaRepository<Advertisement, Long> {
    override fun findById(id: Long): Optional<Advertisement>

    @Query(
        value = "SELECT * FROM ads a " +
                "WHERE to_tsvector(a.description) @@ plainto_tsquery(:query) = TRUE " +
                "or to_tsvector(a.title) @@ plainto_tsquery(:query) = TRUE",
        nativeQuery = true
    )
    fun findByQuery(query: String?, pageable: Pageable?): Page<Advertisement>

    fun findAllByOwnerId(id: Long, pageable: Pageable): Page<Advertisement>
    fun findAllByCategory(category: String, pageable: Pageable): Page<Advertisement>
}