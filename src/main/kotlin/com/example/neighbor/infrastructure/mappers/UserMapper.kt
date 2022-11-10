package com.example.neighbor.infrastructure.mappers

import com.example.neighbor.dto.UserPublicDto
import com.example.neighbor.models.User
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.springframework.stereotype.Component

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
interface UserMapper {
    fun userToUserPublicDto(user: User): UserPublicDto
}