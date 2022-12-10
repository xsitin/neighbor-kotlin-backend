package ru.neighbor.repostories

import ru.neighbor.models.Advertisement
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AdvertisementRepository : JpaRepository<Advertisement, Long> {
    override fun findById(id: Long): Optional<Advertisement>
    fun findAllByOwnerId(id: Long, pageable: Pageable): Page<Advertisement>
}