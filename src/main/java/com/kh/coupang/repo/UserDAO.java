package com.kh.coupang.repo;

import com.kh.coupang.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, String> {

}
