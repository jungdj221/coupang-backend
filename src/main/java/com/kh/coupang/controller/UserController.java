package com.kh.coupang.controller;

import com.kh.coupang.domain.User;
import com.kh.coupang.domain.UserDTO;
import com.kh.coupang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signUp")
    public ResponseEntity create(@RequestBody User vo){

        User user = User.builder()
                .id(vo.getId())
                .password(passwordEncoder.encode(vo.getPassword()))
                .name(vo.getName())
                .phone(vo.getPhone())
                .email(vo.getEmail())
                .address(vo.getAddress())
                .role("ROLE_USER")
                .build();

        User result = userService.create(user);
        UserDTO responseDTO = UserDTO.builder()
                .id(result.getId())
                .name(result.getName())
                .build();
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/login")
}
