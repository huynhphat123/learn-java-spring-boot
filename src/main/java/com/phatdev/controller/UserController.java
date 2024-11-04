package com.phatdev.controller;

import com.phatdev.dto.request.ApiReponse;
import com.phatdev.dto.request.UserCreateRequest;
import com.phatdev.dto.request.UserUpdateRequest;
import com.phatdev.dto.response.UserResponse;
import com.phatdev.entity.User;
import com.phatdev.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là lớp Controller, dùng để xử lý các request từ phía client
@RequestMapping("/users")  // Định tuyến các yêu cầu đến /users
@RequiredArgsConstructor //  Sinh ra một constructor tự động, chứa các tham số là tất cả các trường final hoặc non-null trong class
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    // tạo thông tin user và gọi xuống service
    @PostMapping
    ApiReponse<User> createUser(@RequestBody @Valid UserCreateRequest request) {
     ApiReponse<User> apiResponse = new ApiReponse<>();

     apiResponse.setResult(userService.createUser(request));
      return apiResponse;
    }

    // Lấy danh sách tất cả user và gọi xuống service
    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    // Lấy thông tin user theo id và gọi xuống service
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
            return userService.getUser(userId);
    }

    //  Cập nhật thông tin người dùng và gọi xuống service
    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    // // Xóa người dùng theo ID và gọi xuống service
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId){
         userService.deleteUser(userId);
         return "User has been deleted";
    }
}
