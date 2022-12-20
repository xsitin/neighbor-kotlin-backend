package ru.neighbor.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import ru.neighbor.dto.AdvertisementDto
import ru.neighbor.dto.CreateAdvertisementDto
import ru.neighbor.dto.PaginationInfo
import ru.neighbor.infrastructure.mappers.AdvertisementMapper
import ru.neighbor.services.AdvertisementService
import ru.neighbor.services.ImageService
import ru.neighbor.services.UserService


@RestController
@CrossOrigin
@RequestMapping("ads")
class AdvertisementController(
    private val advertisementService: AdvertisementService,
    private val advertisementMapper: AdvertisementMapper,
    private val imageService: ImageService,
    private val userService: UserService
) {
    @GetMapping("getbyid/{id}")
    @ResponseBody
    operator fun get(@PathVariable id: Long): AdvertisementDto {
        val advertisement = advertisementService.getById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found") }

        return advertisementMapper.advertisementToAdvertisementDto(advertisement)
    }

    @GetMapping("get")
    @ResponseBody
    fun getPopular(@RequestParam page: Int, @RequestParam pageSize: Int): PaginationInfo<AdvertisementDto> {
        val advertisements = advertisementService.getAdvertisements(page - 1, pageSize)
        val advertisementDtos = ArrayList<AdvertisementDto>()
        for (ad in advertisements) {
            advertisementDtos.add(advertisementMapper.advertisementToAdvertisementDto(ad))
        }
        return PaginationInfo(page, pageSize, advertisementDtos.size, advertisements.totalPages, advertisementDtos)
    }

    @GetMapping("getUserAds/{login}")
    @ResponseBody
    fun getUserAds(
        @PathVariable login: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): PaginationInfo<AdvertisementDto> {
        val user = userService.getUser(login) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        val advertisements = advertisementService.getAdvertisements(user.id, page - 1, pageSize)
        val advertisementDtos = ArrayList<AdvertisementDto>()

        for (advertisement in advertisements) {
            advertisementDtos.add(advertisementMapper.advertisementToAdvertisementDto(advertisement))
        }

        return PaginationInfo(page, pageSize, advertisementDtos.size, advertisements.totalPages, advertisementDtos)
    }


    @GetMapping("categories")
    @ResponseBody
    fun getWithCategory(
        @RequestParam category: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): PaginationInfo<AdvertisementDto> {
        val advertisements = advertisementService.getAdvertisements(category, page - 1, pageSize)
        val advertisementDtos = advertisements.map(advertisementMapper::advertisementToAdvertisementDto).toList()

        return PaginationInfo(
            page,
            pageSize,
            advertisementDtos.size,
            advertisements.totalPages,
            advertisementDtos
        )
    }

    @GetMapping("search")
    @ResponseBody
    fun searchAdvertisement(
        @RequestParam query: String,
        @RequestParam page: Int,
        @RequestParam pageSize: Int
    ): PaginationInfo<AdvertisementDto> {
        val advertisements = advertisementService.findAds(query, page - 1, pageSize)
        val advertisementDtos = advertisements.map(advertisementMapper::advertisementToAdvertisementDto).toList()

        return PaginationInfo(
            page,
            pageSize,
            advertisementDtos.size,
            advertisements.totalPages,
            advertisementDtos
        )
    }

    @DeleteMapping("{id}/delete")
    @ResponseBody
    fun remove(auth: Authentication, @PathVariable id: Long) {
        val user =
            userService.getUser(auth.name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        val advertisement = advertisementService.getById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found") }

        if (advertisement.owner.id != user.id) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "not your business"
            )
        }

        advertisementService.remove(advertisement)
    }

    @PostMapping(value = ["add"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseBody
    fun add(
        auth: Authentication,
        @RequestPart("Ad") createDto: CreateAdvertisementDto,
        @RequestPart("images") filesList: List<MultipartFile>
    ): AdvertisementDto? {
        val advertisement = advertisementMapper.createAdvertisementDtoToAdvertisement(createDto)
        val images = imageService.createImagesFromMultipartFile(filesList)

        advertisement.images = images
        advertisement.owner =
            userService.getUser(auth.name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")

        val created = advertisementService.create(advertisement)
        return advertisementMapper.advertisementToAdvertisementDto(created)
    }

    @PatchMapping("update")
    @ResponseBody
    fun update(
        auth: Authentication,
        @RequestPart("Ad") advertisementDto: AdvertisementDto,
        @RequestPart("images") filesList: List<MultipartFile>
    ): AdvertisementDto {
        val user =
            userService.getUser(auth.name) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        var advertisement = advertisementService.getById(advertisementDto.id.toLong())
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "advertisement not found") }

        if (advertisement.owner.id != user.id) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "not your business"
            )
        }

        val rating = advertisement.rating
        advertisement = advertisementMapper.advertisementDtoToAdvertisement(advertisementDto)
        advertisement.rating = rating

        val images = imageService.createImagesFromMultipartFile(filesList)
        advertisement.images = images
        advertisement.owner = user

        val updated = advertisementService.update(advertisement)
        return advertisementMapper.advertisementToAdvertisementDto(updated)
    }
}