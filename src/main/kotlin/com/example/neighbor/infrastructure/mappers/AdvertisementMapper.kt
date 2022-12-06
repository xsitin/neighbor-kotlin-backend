package com.example.neighbor.infrastructure.mappers

import com.example.neighbor.dto.AdvertisementDto
import com.example.neighbor.dto.CreateAdvertisementDto
import com.example.neighbor.models.Advertisement
import com.example.neighbor.models.Image
import org.mapstruct.*
import org.springframework.stereotype.Component

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    uses = [ImageMapper::class]
)
@Component
interface AdvertisementMapper {
    @Mapping(target = "ownerName", source = "advertisement.owner.name")
    @Mapping(target = "imagesIds", source = "advertisement.images")
    fun advertisementToAdvertisementDto(advertisement: Advertisement): AdvertisementDto

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "images", ignore = true)
    fun createAdvertisementDtoToAdvertisement(advertisement: CreateAdvertisementDto): Advertisement
}