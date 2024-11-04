package com.phatdev.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity // Đánh dấu class này là một entity (thực thể) để ánh xạ với bảng trong cơ sở dữ liệu
@Data // Tạo getter, setter, toString và equals/hashCode tự động
public class User {
    @Id // Đánh dấu trường id là khóa chính của bảng
    @GeneratedValue(strategy = GenerationType.UUID) // Sử dụng UUID làm giá trị khóa chính
    private String id; // ID người dùng
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob; // Ngày sinh
}
