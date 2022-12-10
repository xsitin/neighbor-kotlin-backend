package ru.neighbor.services

import ru.neighbor.models.Advertisement
import ru.neighbor.repostories.AdvertisementRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.util.*
import javax.transaction.Transactional

@Component
class AdvertisementService(val repository: AdvertisementRepository) {
    @Transactional
    fun getAds(page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.unsorted())
        return repository.findAll(pagination)
    }

    @Transactional
    fun getAds(ownerId: Long, page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.unsorted())
        return repository.findAllByOwnerId(ownerId, pagination)
    }

    @Transactional
    fun create(advertisement: Advertisement): Advertisement {
        return repository.save(advertisement)
    }

    @Transactional
    fun remove(ad: Advertisement) {
        repository.delete(ad)
    }

    @Transactional
    fun getById(id: Long): Optional<Advertisement> {
        return repository.findById(id)
    }
}