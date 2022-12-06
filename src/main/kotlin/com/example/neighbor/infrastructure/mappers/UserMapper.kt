package com.example.neighbor.infrastructure.mappers

import com.example.neighbor.dto.UserAuthDto
import com.example.neighbor.dto.UserPublicDto
import com.example.neighbor.dto.UserRegistrationDto
import com.example.neighbor.models.User
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.springframework.stereotype.Component

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
interface UserMapper {
    @Mapping(target = "avatarId", source = "user.avatar.id")
    fun userToUserPublicDto(user: User): UserPublicDto

    @Mapping(target = "avatar", ignore = true)
    fun userRegisterDtoToUser(user: UserRegistrationDto): User

    fun userToUserAuthDto(user: User): UserAuthDto
}