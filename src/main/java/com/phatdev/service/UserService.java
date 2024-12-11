package com.phatdev.service;

import com.phatdev.dto.request.UserCreateRequest;
import com.phatdev.dto.request.UserUpdateRequest;
import com.phatdev.dto.response.UserResponse;
import com.phatdev.entity.User;
import com.phatdev.enums.Role;
import com.phatdev.expection.AppException;
import com.phatdev.expection.ErrorCode;
import com.phatdev.mapper.UserMapper;
import com.phatdev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service // Đánh dấu lớp này là một Service trong Spring, chịu trách nhiệm xử lý logic nghiệp vụ
@RequiredArgsConstructor // Tự động tạo constructor cho các trường final (userRepository, userMapper)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Đặt tất cả các trường là private và final (không thay đổi được sau khi khởi tạo)

public class UserService {

     UserRepository userRepository; // Repository giao tiếp với cơ sở dữ liệu
     UserMapper userMapper; // Mapper để chuyển đổi giữa DTO và entity
    PasswordEncoder passwordEncoder;

    //  Tạo user mới và lưu vào cơ sở dữ liệu
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) // Kiểm tra xem user đã tồn tại hay chưa
            throw new AppException(ErrorCode.USER_EXISTED); // Nếu user đã tồn tại, ném ngoại lệ

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));  // So sánh mật khẩu nhập vào với mật khẩu đã mã hóa trong DB

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);


        return userRepository.save(user);
    }
    // Lấy danh sách tất cả người dùng
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    // Lấy thông tin người dùng theo ID
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id) // nếu tìm thấy => id
                .orElseThrow(() -> new RuntimeException("User Not Found"))); // nếu kh tìm thấy => TB lỗi
    }

    // Cập nhật thông tin người dùng
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found")); // Nếu không tìm thấy user, ném ngoại lệ
        userMapper.updateUser(user, request); // Sử dụng MapStruct để cập nhật thông tin user từ request

        return userMapper.toUserResponse(userRepository.save(user)); // Lưu user đã cập nhật và trả về DTO
    }

    // Xóa người dùng theo ID
    public void deleteUser(String userId) {
        userRepository.deleteById(userId); // Xóa user theo ID
    }


}
