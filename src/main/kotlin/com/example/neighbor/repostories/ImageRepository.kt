package com.example.neighbor.repostories

import com.example.neighbor.models.Image
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : CrudRepository<Image?, Long?>