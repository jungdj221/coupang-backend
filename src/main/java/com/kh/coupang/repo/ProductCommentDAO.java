package com.kh.coupang.repo;

import com.kh.coupang.domain.ProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCommentDAO extends JpaRepository<ProductComment, Integer> {

    // 상품 1개에 따른 댓글 전체 조회                                               짠 쿼리문대로 가져오겠다 ; true

    @Query(value = "SELECT * FROM product_comment WHERE prod_code =:prodCode", nativeQuery = true)
    List<ProductComment> findByProdCode(@Param("prodCode") int prodCode);
}
