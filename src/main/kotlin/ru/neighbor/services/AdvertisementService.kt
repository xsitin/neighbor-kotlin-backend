package ru.neighbor.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.neighbor.models.Advertisement
import ru.neighbor.repostories.AdvertisementRepository
import java.util.*

@Component
class AdvertisementService(val repository: AdvertisementRepository, val ratingCalculator: RatingCalculator) {
    fun getAdvertisements(page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("rating")))
        return repository.findAll(pagination)
    }

    fun getAdvertisements(ownerId: Long, page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("rating")))
        return repository.findAllByOwnerId(ownerId, pagination)
    }

    fun getAdvertisements(category: String, page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("rating")))
        return repository.findAllByCategory(category, pagination)
    }

    fun findAds(query: String?, page: Int, pageSize: Int): Page<Advertisement> {
        val pagination = PageRequest.of(page, pageSize, Sort.unsorted())
        return repository.findByQuery(query, pagination)
    }

    @Transactional
    fun create(advertisement: Advertisement): Advertisement {
        advertisement.rating = ratingCalculator.getRating(advertisement)
        return repository.save(advertisement)
    }

    fun update(advertisement: Advertisement): Advertisement {
        return repository.save(advertisement)
    }

    @Transactional
    fun remove(ad: Advertisement) {
        repository.delete(ad)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getById(id: Long): Optional<Advertisement> {
        val advertisement = repository.findById(id)

        advertisement.ifPresent { ad ->
            ad.rating = ad.rating + 1
            repository.save(ad)
        }

        return advertisement
    }
}