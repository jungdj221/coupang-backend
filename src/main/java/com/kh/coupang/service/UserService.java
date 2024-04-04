package com.kh.coupang.service;

import com.kh.coupang.domain.User;
import com.kh.coupang.repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDao;

    // 회원가입
    public User create(User user){
        return userDao.save(user);
    }

    // 로그인 - 사용자확인
    public User login(String id, String password, PasswordEncoder encoder){
        User user = userDao.findById(id).orElse(null);
        if(user!=null &&  encoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }
}
