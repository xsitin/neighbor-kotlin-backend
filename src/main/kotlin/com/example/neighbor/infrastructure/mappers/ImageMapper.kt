package com.example.neighbor.infrastructure.mappers

import com.example.neighbor.models.Image
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.springframework.stereotype.Component

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
abstract class ImageMapper {
    fun imageToImageId(image: Image): String {
        return image.id.toString()
    }
}