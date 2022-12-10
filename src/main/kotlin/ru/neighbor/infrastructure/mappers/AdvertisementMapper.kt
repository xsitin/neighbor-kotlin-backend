package ru.neighbor.infrastructure.mappers

import ru.neighbor.dto.AdvertisementDto
import ru.neighbor.dto.CreateAdvertisementDto
import ru.neighbor.models.Advertisement
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
    @Mapping(target = "ownerName", source = "advertisement.owner.login")
    @Mapping(target = "imagesIds", source = "advertisement.images")
    fun advertisementToAdvertisementDto(advertisement: Advertisement): AdvertisementDto

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "images", ignore = true)
    fun createAdvertisementDtoToAdvertisement(advertisement: CreateAdvertisementDto): Advertisement
}