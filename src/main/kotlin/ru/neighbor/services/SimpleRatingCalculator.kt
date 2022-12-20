package ru.neighbor.services

import org.springframework.stereotype.Service
import ru.neighbor.models.Advertisement

@Service
class SimpleRatingCalculator : RatingCalculator {
    override fun getRating(ad: Advertisement): Int {
        var rating = 0

        rating += ad.description.length / 10
        rating += ad.images.size

        return rating
    }
}