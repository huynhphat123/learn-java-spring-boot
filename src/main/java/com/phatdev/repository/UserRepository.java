package com.phatdev.repository;

import com.phatdev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Đánh dấu interface này là repository, quản lý dữ liệu trong cơ sở dữ liệu
public interface UserRepository extends JpaRepository<User, String> {
    // Kiểm tra xem username đã tồn tại chưa
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
