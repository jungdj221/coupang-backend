package com.kh.coupang.repo;

import com.kh.coupang.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDAO extends JpaRepository<Review, Integer> {
}
