package com.kh.coupang.controller;

import com.kh.coupang.domain.*;
import com.kh.coupang.service.ReviewCommentService;
import com.kh.coupang.service.ReviewService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/*")
@CrossOrigin(origins ={"*"}, maxAge = 6000)
public class ReviewController {

    @Autowired
    private ReviewService review;

    @Autowired
    private ReviewCommentService comment;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @PostMapping("/review")
    public ResponseEntity<Review> create(ReviewDTO dto) throws IOException {

        // review 부터 추가하여 revi_code가 담긴 review
        Review vo = new Review();
        vo.setId(dto.getId());
        vo.setProdCode(dto.getProdCode());
        vo.setReviTitle(dto.getReviTitle());
        vo.setReviDesc(dto.getReviDesc());
        vo.setRating(dto.getRating());
        Review result = review.create(vo);
        log.info("result : " + result);


        log.info("dto : " + dto);
        //review_image에는 revi_code 가 필요
        for(MultipartFile file :  dto.getFiles()) {
            log.info(file.getOriginalFilename());

            String fileName =  file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + "review" + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            file.transferTo(savePath);

            ReviewImage image = new ReviewImage();
            image.setReviUrl(saveName);
            image.setReview(result);
            review.createImg(image);

        }

        return result!=null ? ResponseEntity.status(HttpStatus.CREATED).body(result) :
                                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 상품 1개에 따른 리뷰(댓글)전체 보기
    @GetMapping("/public/product/{code}/review")
    public ResponseEntity<List<Review>> viewAll(@RequestParam(name = "page", defaultValue = "1") int page, @PathVariable(name = "code") int code){
        log.info("page : " + page);

        Sort sort = Sort.by("reviCode").descending();

        Pageable pageable = PageRequest.of(page-1, 10, sort);
//        Page<Review> list = review.viewAll(pageable, builder);

        QReview qReview = QReview.review;

        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qReview.prodCode.eq(code);
        builder.and(expression);



        return ResponseEntity.status(HttpStatus.OK).body(review.viewAll(pageable, builder).getContent());
    }

    public Object authentication(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication.getPrincipal();
    }

    // 리뷰글 하나에 댓글 달기
    @PostMapping("/review/comment")
    public ResponseEntity createComment(@RequestBody ReviewComment vo){
        // 시큐리티에 있는 사용자 정보 가져오기
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication();

        if(principal instanceof User){ // principal에 유저정보가 담겨있다면
            User user = (User) principal;
            vo.setUser(user);
            return ResponseEntity.ok(comment.create(vo));
        }

        log.info("vo : " + vo);
        return ResponseEntity.badRequest().build();
    }

    // 특정 리뷰글에 따른 그에 해당하는 댓글 조회 -> 여기도 로그인에 관계없이 보여질수 있게하기
    @GetMapping("/public/review/{reviCode}/comment")
    public ResponseEntity<List<ReviewCommentDTO>> viewComment(@PathVariable(name = "reviCode") int reviCode){
        List<ReviewComment> topList = comment.getTopLevelComments(reviCode);
        List<ReviewCommentDTO> response = new ArrayList<>();  // 상위 댓글 담기

        for (ReviewComment top : topList){
            List<ReviewComment> replies = comment.getRepliesComments(top.getReviComCode(), reviCode);
            List<ReviewCommentDTO> repliesDTO = new ArrayList<>();
            for (ReviewComment reply : replies){
                ReviewCommentDTO dto = ReviewCommentDTO.builder()
                        .reviCode(reply.getReviCode())
                        .reviComeCode(reply.getReviComCode())
                        .reviComeDesc(reply.getReviComDesc())
                        .reviComDate(reply.getReviComDate())
                        .user(UserDTO.builder()
                                .id(reply.getUser().getId())
                                .name(reply.getUser().getName())
                                .build())
                        .build();
                repliesDTO.add(dto);
            }
            ReviewCommentDTO dto = ReviewCommentDTO.builder()
                    .reviCode(top.getReviCode())
                    .reviComeCode(top.getReviComCode())
                    .reviComeDesc(top.getReviComDesc())
                    .reviComDate(top.getReviComDate())
                    .user(UserDTO.builder()
                            .id(top.getUser().getId())
                            .name(top.getUser().getName())
                            .build())
                    .replies(repliesDTO)
                    .build();
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

}
