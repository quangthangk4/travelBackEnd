package com.travel.travelling.mapper;

import com.travel.travelling.dto.request.UserCreationRequest;
import com.travel.travelling.dto.request.UserUpdateRequest;
import com.travel.travelling.dto.response.UserResponse;
import com.travel.travelling.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
