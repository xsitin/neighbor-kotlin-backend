package ru.neighbor.infrastructure.mappers

import ru.neighbor.models.Role
import org.mapstruct.EnumMapping
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.springframework.stereotype.Component

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@Component
abstract class RoleMapper {
    @EnumMapping
    fun roleToInt(role: Role): Int {
        return role.ordinal
    }
}