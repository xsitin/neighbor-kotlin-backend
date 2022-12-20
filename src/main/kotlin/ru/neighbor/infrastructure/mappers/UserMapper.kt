package ru.neighbor.infrastructure.mappers

import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.springframework.stereotype.Component
import ru.neighbor.dto.UserAuthDto
import ru.neighbor.dto.UserFullDto
import ru.neighbor.dto.UserPublicDto
import ru.neighbor.dto.UserRegistrationDto
import ru.neighbor.models.User

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = [RoleMapper::class]
)
@Component
interface UserMapper {
    @Mapping(target = "avatarId", source = "user.avatar.id")
    fun userToUserPublicDto(user: User): UserPublicDto

    @Mapping(target = "avatar", ignore = true)
    fun userRegisterDtoToUser(user: UserRegistrationDto): User

    fun userToUserAuthDto(user: User): UserAuthDto

    @Mapping(target = "avatarId", source = "user.avatar.id")
    fun userToUserFullDto(user: User): UserFullDto
}