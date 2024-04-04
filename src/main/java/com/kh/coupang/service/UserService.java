package com.kh.coupang.service;

import com.kh.coupang.domain.User;
import com.kh.coupang.repo.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDao;

    // 회원가입
    public User create(User user){
        return userDao.save(user);
    }
}
