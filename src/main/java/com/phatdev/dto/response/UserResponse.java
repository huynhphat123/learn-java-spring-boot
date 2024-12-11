package com.phatdev.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;


@JsonInclude(JsonInclude.Include.NON_NULL) // Loại bỏ các thuộc tính có giá trị null khi trả về JSON
@Data // Lombok tự động tạo getter, setter, toString, equals và hashCode
@NoArgsConstructor // Lombok tự động tạo constructor không tham số
@AllArgsConstructor // Lombok tự động tạo constructor có tham số cho tất cả các trường
@Builder // Tạo Builder pattern cho class này
@FieldDefaults(level = AccessLevel.PRIVATE) // Đặt tất cả các trường là private
public class UserResponse {
     String id; // ID người dùng
     String username;
     String password;
     String firstName;
     String lastName;
     LocalDate dob; // Ngày sinh
     Set<String> roles;
}
