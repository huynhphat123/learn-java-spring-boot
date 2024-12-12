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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Đánh dấu đây là lớp Controller, dùng để xử lý các request từ phía client
@RequestMapping("/users")  // Định tuyến các yêu cầu đến /users
@RequiredArgsConstructor //  Sinh ra một constructor tự động, chứa các tham số là tất cả các trường final hoặc non-null trong class
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    // tạo thông tin user và gọi xuống service
    @PostMapping
    ApiReponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        return ApiReponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    // Lấy danh sách tất cả user và gọi xuống service
    @GetMapping
    ApiReponse<List<UserResponse>> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username : {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return ApiReponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiReponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiReponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/myInfo")
    ApiReponse<UserResponse> getMyInfo(){
        return ApiReponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiReponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiReponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @PutMapping("/{userId}")
    ApiReponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiReponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
