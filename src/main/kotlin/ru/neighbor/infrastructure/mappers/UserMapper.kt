package ru.neighbor.infrastructure.mappers

import ru.neighbor.dto.UserAuthDto
import ru.neighbor.dto.UserPublicDto
import ru.neighbor.dto.UserRegistrationDto
import ru.neighbor.models.User
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