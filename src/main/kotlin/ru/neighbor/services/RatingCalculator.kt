package ru.neighbor.services

import ru.neighbor.models.Advertisement

interface RatingCalculator {
    fun getRating(ad: Advertisement): Int
}