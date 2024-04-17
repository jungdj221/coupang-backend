package com.kh.coupang.controller;

import com.kh.coupang.config.TokenProvider;
import com.kh.coupang.domain.User;
import com.kh.coupang.domain.UserDTO;
import com.kh.coupang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins ={"*"}, maxAge = 6000)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;
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
    public ResponseEntity login(@RequestBody User vo){
        User user = userService.login(vo.getId(), vo.getPassword(), passwordEncoder);
        if (user!=null){
            // 토큰 생성. 로그인 성공 정보를 시큐리티에서 처리하게 위해서
            String token = tokenProvider.create(user);

            // 로그인 성공
            UserDTO responseDTO = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } else{
            // 로그인 실패
            return ResponseEntity.badRequest().build();
        }

    }
}
