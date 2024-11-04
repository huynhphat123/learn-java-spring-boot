package com.phatdev.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data // Tạo sẵn getter và setter cho các thuộc tính
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    // Các trường cần thiết để cập nhật thông tin người dùng
     String password;
     String firstName;
     String lastName;
     LocalDate dob;
}
