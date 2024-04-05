package com.kh.coupang.service;


import com.kh.coupang.domain.QReviewComment;
import com.kh.coupang.domain.ReviewComment;
import com.kh.coupang.repo.ReviewCommentDAO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewCommentService {

    @Autowired
    private ReviewCommentDAO dao;

    @Autowired
    private JPAQueryFactory queryFactory;


    private final QReviewComment qReviewComment = QReviewComment.reviewComment;

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
}
