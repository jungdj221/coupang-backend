package com.kh.coupang.service;

import com.kh.coupang.domain.*;
import com.kh.coupang.repo.ReviewDAO;
import com.kh.coupang.repo.ReviewImageDAO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewDAO review;

    @Autowired
    private ReviewImageDAO image;

    @Autowired
    private JPAQueryFactory queryFactory;


    private final QReviewComment qReviewComment = QReviewComment.reviewComment;
    private final QReview qReview = QReview.review;

    private final QReviewImage qReviewImage = QReviewImage.reviewImage;

    public Review create(Review vo){
        return review.save(vo);
    }

    public ReviewImage createImg(ReviewImage vo){
        return image.save(vo);
    }

    public Page<Review> viewAll(Pageable pageable, BooleanBuilder builder){
        return review.findAll(builder, pageable ); // findALL이 builder를 먼저 봐야함
    }

    // 특정 review글 하나 불러오기 - 그냥 service에서 삭제하기
    public void getOneReview(int reviCode){
        review.deleteById(reviCode);

//        Review comment = queryFactory.selectFrom(qReview)
//                .where(qReview.reviCode.eq(reviCode))
//                .fetchFirst();
//        if (comment !=null){
//            review.delete(comment);
//        }
    }

    // 특정 review글 하나의 이미지리스트 가져오기 - querydsl을 쓸 경우 dao 안 거쳐도 됨.
    public List<ReviewImage> getImageList(int reviCode){
        return queryFactory.selectFrom(qReviewImage)
                .where(qReview.reviCode.eq(reviCode))
                .fetch();
    }
    // dao의 query를 거친 과정
    public List<ReviewImage> reviewImages(int reviCode){
        return image.findByRevicode(reviCode);
    }
    // 이제 image테이블에서 삭제를 해야함 reviImageCode = reviCode
    public void deleteImage(int reviCode){
        image.deleteById(reviCode);

//       image.deleteAll(queryFactory.selectFrom(qReviewImage)
//               .where(qReviewImage.reviImgCode.eq(reviCode))
//               .fetch());
    }



}
