package com.phatdev.mapper;

import com.phatdev.dto.request.UserCreateRequest;
import com.phatdev.dto.request.UserUpdateRequest;
import com.phatdev.dto.response.UserResponse;
import com.phatdev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Đánh dấu đây là Mapper, MapStruct sẽ tự động sinh ra class để thực hiện mapping
public interface UserMapper {

    // Chuyển từ UserCreateRequest sang User entity
    User toUser(UserCreateRequest request);

    // Cập nhật các giá trị trong User bằng các thuộc tính từ request
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    // Chuyển từ User entity sang UserResponse DTO
    UserResponse toUserResponse(User user);
}
