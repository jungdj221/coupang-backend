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
    log.info("dto : " + dto);
        // review 부터 추가하여 revi_code가 담긴 review
        Review vo = new Review();
        vo.setId(dto.getId());
        vo.setProdCode(dto.getProdCode());
        vo.setReviTitle(dto.getReviTitle());
        vo.setReviDesc(dto.getReviDesc());
        vo.setRating(dto.getRating());
        Review result = review.create(vo);
        log.info("result : " + result);


//        log.info("dto : " + dto);
        //review_image에는 revi_code 가 필요
        if(dto.getFiles() !=null){
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

    @DeleteMapping("/review/{code}")
    public ResponseEntity delete(@PathVariable(name="code") int code) {
        // code : 리뷰 코드! reviCode
        // 이미지들 삭제하면서 리뷰 이미지 테이블에서도 삭제
        // 1. 이미지 테이블에서 해당 reviCode에 대한 이미지들 가지고 와야죠! (List<ReviewImage>)
        //      ---> SELECT 문 생각해보고! DAO에 추가해서 Service에 반영해서 가지고 오면 됨!
        //      ---> QueryDSL 방법으로도 가지고 올 수 있죠!
        List<ReviewImage> images = review.getImageList(code);

        for(ReviewImage image : images) {
            // 2. 반복문을 돌려서 각각의 image에 있는 URL(reviUrl)로 File 객체로 file.delete() 사용!
            //  ---> 실제 폴더에 있는 이미지 파일 삭제
            File file = new File(image.getReviUrl());
            file.delete();

            // 3. 반복문 안에서 그와 동시에 이미지 테이블에서 이미지의 Code로 삭제 기능 진행(reviImgCode)
            //  ---> DB에 저장한 이미지 정보 삭제
            review.deleteImage(image.getReviImgCode());
        }

        // 리뷰 삭제 --> reviCode로 삭제!
        review.getOneReview(code);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/review")
    public  ResponseEntity update(ReviewDTO dto) throws IOException {
        log.info("dto : " + dto);
        //리뷰코드 가지고 와서
        // 이미지는 dto.images/files에 다 있음 .image에는 삭제 안한 것들null이면 기존거 다 삭제 / file에는 새로 추가된 사진이 있음
        //1. 기존 리뷰에 있던 이미지들 정보 가져오기
        List<ReviewImage> prevImages = review.reviewImages(dto.getReviCode());
//        log.info("prevImage :" + prevImages);
        // 2. 반복문으로 dto.images에 해당 이미지가 포함되어 있는지 판단 dto.getImages().contains(image.getReviUrl()), log 찍어보기
        for(ReviewImage image : prevImages){
            //dto.getImages().contains(image.getReviUrl())
            // 3. 위의 조건을 걸어서 실제 파일 삭제
            if(dto.getImages()!=null && !dto.getImages().contains(image.getReviUrl()) || dto.getImages() == null){
//                log.info("삭제");
//                log.info("삭제파일 : " + image.getReviUrl());
                File file = new File(image.getReviUrl());
                file.delete();
                // 4. 파일 삭제와 동시에 테이블에서도 해당 가상 정보 삭제
                review.deleteImage(image.getReviImgCode());
            }
        }
        if(dto.getFiles() !=null){

            ReviewImage imgVo = new ReviewImage();

            for(MultipartFile file :  dto.getFiles()) {
                log.info(file.getOriginalFilename());

                String fileName =  file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String saveName = uploadPath + File.separator + "review" + File.separator + uuid + "_" + fileName;
                Path savePath = Paths.get(saveName);
                file.transferTo(savePath);

                imgVo.setReviUrl(saveName);
                imgVo.setReview(Review.builder().reviCode(dto.getReviCode()).build());
                review.createImg(imgVo);

            }
        }


        // 리뷰 수정
        Review vo = Review.builder()
                .reviCode(dto.getReviCode())
                .id(dto.getId())
                .prodCode(dto.getProdCode())
                .reviTitle(dto.getReviTitle())
                .reviDesc(dto.getReviDesc())
                .build();
        review.create(vo);
        return ResponseEntity.ok().build();
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
