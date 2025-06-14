package com.subash.user.management.mapper;

import com.subash.user.management.model.Role;
import com.subash.user.management.model.User;
import com.subash.user.management.model.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "role", expression = "java(mapToEntityRole(userView.getRole()))")
    User userViewToUser(UserView userView);

    @Mapping(target = "role", expression = "java(mapToViewRole(user.getRole()))")
    @Mapping(target = "passwordHash", ignore = true)
    UserView userToUserView(User user);

    default Role mapToEntityRole(UserView.RoleEnum roleEnum) {
        return roleEnum != null ? Role.valueOf(roleEnum.name()) : null;
    }

    default UserView.RoleEnum mapToViewRole(Role role) {
        return role != null ? UserView.RoleEnum.valueOf(role.name()) : null;
    }
}
