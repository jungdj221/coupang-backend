package com.kh.coupang.repo;

import com.kh.coupang.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageDAO extends JpaRepository<ReviewImage,Integer> {
    
    // 특정 리뷰의 모든 이미지 조회
    // SELECT * FROM review_image WHERE revi_code 
    @Query(value = "SELECT * FROM review_image WHERE revi_code = :code", nativeQuery = true)
    List<ReviewImage> findByRevicode(@Param("code") Integer code); // null경우 처리를 위해서 Integer로 둠
}
