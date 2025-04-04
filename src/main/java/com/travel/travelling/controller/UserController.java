package com.travel.travelling.controller;

import com.travel.travelling.dto.request.UserCreationRequest;
import com.travel.travelling.dto.request.UserUpdateRequest;
import com.travel.travelling.dto.response.ApiResponse;
import com.travel.travelling.dto.response.UserResponse;
import com.travel.travelling.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("/getAllUsers")
    public ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/getMyInfo")
    public ApiResponse<UserResponse> getInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }


    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .message("user has been deleted")
                .build();
    }

    @PutMapping("/deposit/{amount}")
    public ApiResponse<Void> depositMoney(@PathVariable double amount){
        userService.depositMoney(amount);
        return ApiResponse.<Void>builder()
                .message("deposit money successful")
                .build();
    }
}
