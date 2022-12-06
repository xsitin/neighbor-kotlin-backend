package com.example.neighbor.services

import com.example.neighbor.models.Image
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.util.*
import com.example.neighbor.repostories.ImageRepository

@Service
class ImageService(repository: ImageRepository) {
    private val repository: ImageRepository

    init {
        this.repository = repository
    }

    fun createImage(data: ByteArray): Image {
        val image = Image(data)
        return repository.save(image)
    }

    fun deleteImage(id: Long) {
        repository.deleteById(id)
    }

    fun getImage(id: Long): Optional<Image?> {
        return repository.findById(id)
    }

    fun createImagesFromMultipartFile(filesList: List<MultipartFile>): List<Image> {
        val imagesContent = ArrayList<ByteArray>()

        try {
            for (image in filesList) {
                imagesContent.add(image.bytes)
            }
        } catch (ignored: IOException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "incorrect data")
        }

        val images = ArrayList<Image>()
        for (imageContent in imagesContent) {
            val createdImage = createImage(imageContent)
            images.add(createdImage)
        }

        return images
    }
}