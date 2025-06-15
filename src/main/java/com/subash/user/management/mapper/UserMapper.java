package com.subash.user.management.mapper;

import com.subash.user.management.model.Role;
import com.subash.user.management.model.User;
import com.subash.user.management.model.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * MapStruct mapper interface for converting between User entity and UserView DTO.
 * Also handles role mapping between enum and entity representation.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Static instance of the mapper, usable if not relying on Spring's dependency injection.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a {@link UserView} DTO to a {@link User} entity.
     * <p>
     * - Converts role enum to entity role.
     * - Ignores the password hash to avoid overwriting sensitive fields.
     *
     * @param userView the user DTO received from the client
     * @return User entity to be persisted
     */
    @Mapping(target = "role", expression = "java(mapToEntityRole(userView.getRole()))")
    @Mapping(target = "passwordHash", ignore = true)
    User userViewToUser(UserView userView);

    /**
     * Maps a {@link User} entity to a {@link UserView} DTO.
     * <p>
     * - Converts entity role to enum.
     * - Ignores password field in the view model.
     *
     * @param user the user entity from database
     * @return UserView DTO for external representation
     */
    @Mapping(target = "role", expression = "java(mapToViewRole(user.getRole()))")
    @Mapping(target = "password", ignore = true)
    UserView userToUserView(User user);

    /**
     * Maps a list of {@link User} entities to a list of {@link UserView} DTOs.
     * <p>
     * - Converts entity roles to enums.
     * - Ignores password fields.
     *
     * @param users list of user entities
     * @return list of user DTOs
     */
    @Mapping(target = "role", expression = "java(mapToViewRole(user.getRole()))")
    @Mapping(target = "password", ignore = true)
    List<UserView> userListToUserViewList(List<User> users);

    /**
     * Converts {@link UserView.RoleEnum} to entity {@link Role}.
     *
     * @param roleEnum role enum from view
     * @return entity Role
     */
    default Role mapToEntityRole(UserView.RoleEnum roleEnum) {
        return roleEnum.name().equals("ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER;
    }

    /**
     * Converts entity {@link Role} to {@link UserView.RoleEnum}.
     *
     * @param role role from entity
     * @return RoleEnum for view
     */
    default UserView.RoleEnum mapToViewRole(Role role) {
        return role.name().equals("ROLE_ADMIN") ? UserView.RoleEnum.ADMIN : UserView.RoleEnum.USER;
    }
}
