package com.kh.coupang.service;

import com.kh.coupang.domain.ProductComment;
import com.kh.coupang.domain.QProductComment;
import com.kh.coupang.repo.ProductCommentDAO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCommentService {

    @Autowired
    private ProductCommentDAO dao;

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QProductComment qProductComment = QProductComment.productComment;

    // 댓글 추가 기능
    public ProductComment create(ProductComment vo){
        return dao.save(vo);
    }
//    // 상품 한 개당 해당하는 댓글조회  대댓글 형태로  json이 안 담아짐 이제 안씀
//    public List<ProductComment> findByProdCode(int prodCode){
//        return dao.findByProdCode(prodCode);
//    }

    // 상위 댓글만 조회
    /*
    *   SELECT * FROM product_comment
        WHERE pro_com_parent=0
        AND prod_code = 24
        ORDER BY pro_com_date DESC;
    * */
    public List<ProductComment> getTopLevelComments(int prodCode){
        return queryFactory.selectFrom(qProductComment)
                .where(qProductComment.proComParent.eq(0))
                .where(qProductComment.prodCode.eq(prodCode))
                .orderBy(qProductComment.proComDate.desc())
                .fetch();
    }


    /* 하위 댓글만 조회
    *   SELECT * FROM product_comment
        WHERE pro_com_parent =1
        AND pr
        ORDER BY pro_com_date ASC;
    * */
    public List<ProductComment> getRepliesComments(int parent, int prodCode){
        return queryFactory.selectFrom(qProductComment)
                .where(qProductComment.proComParent.eq(parent))
                .where(qProductComment.prodCode.eq(prodCode))
                .orderBy(qProductComment.proComDate.asc())
                .fetch();
    }
}
