package com.phatdev.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

// UserCreateRequest là một lớp DTO, đóng vai trò chứa dữ liệu đầu vào khi yêu cầu tạo người dùng mới.
@Data // Tạo sẵn các getter, setter, toString, equals và hashCode tự động
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    // Các trường yêu cầu tạo người dùng, có validation cho username và password
    @Size(min = 3, max = 20,message = "USERNAME_INVALID")
     String username;

    @Size(min = 8, max = 15, message = "PASSWORD_INVALID")
     String password;
     String firstName;
     String lastName;
     LocalDate dob; // Ngày sinh của người dùng
}
