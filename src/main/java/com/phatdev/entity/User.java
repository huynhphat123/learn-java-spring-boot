package com.phatdev.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity // Đánh dấu class này là một entity (thực thể) để ánh xạ với bảng trong cơ sở dữ liệu
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id // Đánh dấu trường id là khóa chính của bảng
    @GeneratedValue(strategy = GenerationType.UUID) // Sử dụng UUID làm giá trị khóa chính
     String id; // ID người dùng
     String username;
     String password;
     String firstName;
     String lastName;
     LocalDate dob; // Ngày sinh

    @ElementCollection
    Set<String> roles;
}
