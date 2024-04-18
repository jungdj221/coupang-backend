package com.kh.coupang.service;


import com.kh.coupang.domain.*;
import com.kh.coupang.repo.ReviewCommentDAO;
import com.kh.coupang.repo.ReviewImageDAO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewCommentService {

    @Autowired
    private ReviewCommentDAO dao;

    @Autowired
    private ReviewImageDAO imageDAO;

    @Autowired
    private JPAQueryFactory queryFactory;


    private final QReviewComment qReviewComment = QReviewComment.reviewComment;

    private final QReviewImage qReviewImage = QReviewImage.reviewImage;

    // 리뷰글에 댓글 추가 기능
    public ReviewComment create(ReviewComment vo){
        return dao.save(vo);
    }

    // 상위 댓글 조회하기
    /*
    *   SELECT * FROM review_comment
        WHERE revi_com_parent = 0
        AND revi_code = 4
        ORDER BY revi_com_date DESC;
    * */
    public List<ReviewComment> getTopLevelComments(int reviCode){
        return queryFactory.selectFrom(qReviewComment)
                .where(qReviewComment.ReviComParent.eq(0)) // 상위는 부모코드가 0
                .where(qReviewComment.reviCode.eq(reviCode))
                .orderBy(qReviewComment.reviComDate.asc())
                .fetch();
    }

    // 하위댓글만 불러오기
    public List<ReviewComment> getRepliesComments(int parent, int reviCode){
        return queryFactory.selectFrom(qReviewComment)
                .where(qReviewComment.ReviComParent.eq(parent))
                .where(qReviewComment.reviCode.eq(reviCode))
                .orderBy(qReviewComment.reviComDate.asc())
                .fetch();
    }

//    // 특정 review글 하나 불러오기
//    public ReviewComment getOneReview(int reviCode){
//        ReviewComment comment = queryFactory.selectFrom(qReviewComment)
//                .where(qReviewComment.reviCode.eq(reviCode))
//                .fetchFirst();
//        if (comment !=null){
//            dao.delete(comment);
//        }
//        return  comment;
//    }
//
//    // 특정 review글 하나의 이미지리스트 가져오기 - querydsl을 쓸 경우 dao 안 거쳐도 됨.
//    public List<ReviewImage> getImageList(int reviCode){
//        return queryFactory.selectFrom(qReviewImage)
//                .where(qReviewComment.reviCode.eq(reviCode))
//                .fetch();
//    }
//
//    // dao의 query를 거친 과정
//    public List<ReviewImage> reviewImages(int reviCode){
//        return imageDAO.findByRevicode(reviCode);
//    }
}
