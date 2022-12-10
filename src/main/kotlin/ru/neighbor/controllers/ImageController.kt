package ru.neighbor.controllers

import ru.neighbor.services.ImageService
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("img")
class ImageController(private val imageService: ImageService) {
    @GetMapping("get/{id}")
    fun getImage(@PathVariable id: Long): ByteArray {
        val image = imageService.getImage(id)

        if (image.isEmpty) {
            throw NotFoundException()
        }

        return image.get().data
    }
}