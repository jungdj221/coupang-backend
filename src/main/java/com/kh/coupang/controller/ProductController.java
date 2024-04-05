package com.kh.coupang.controller;

import com.kh.coupang.domain.*;
import com.kh.coupang.service.ProductCommentService;
import com.kh.coupang.service.ProductService;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j  // syso 같은 역할
@RestController
@RequestMapping("/api/*")
public class ProductController {

    @Autowired
    private ProductService service;
    // 메서드명은 service 랑 동일

    @Autowired
    private ProductCommentService comment;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath; // 업로드 경로 역할을 해줌(D:\\upload)


    @GetMapping("/product")                                                     // 이 속성은 받는 것은 필수가 아님                                   아무것도 안 보내면 page=1
    public ResponseEntity<List<Product>> viewAll(@RequestParam(name="category", required = false) Integer category, @RequestParam(name = "page", defaultValue = "1") int page) {
//        log.info("page : " + page);
        Sort sort = Sort.by("prodCode").descending();
        Pageable pageable = PageRequest.of(page-1, 10, sort);

        // QueryDSL
        // 1. 가장 먼저 동적 처리하기 위한 Q도메인 클래스 얻어오기
        // Q도메인 클래스를 이용하면 Entity클래스에 선언된 필드들을 변수로 활용할 수 있음
        QProduct qProduct = QProduct.product;

        // 2. BooleanBuilder : where문에 들어가는 조건들을 넣어주는 컨테이너
        BooleanBuilder builder = new BooleanBuilder();

        if(category!=null){
            // 3. 원하는 조건은 필드값과 같이 결합해서 생성
            BooleanExpression expression = qProduct.category.cateCode.eq(category);

            // 4. 만들어진 조건은 where문에 and나 or 같은 키워드와 결합
            builder.and(expression);
        }

        // 5. BooleanBuilder 는 QuerydslPredicateExcutor 인터페이스의 findAll() 사용
        Page<Product> list = service.viewAll(pageable, builder);
        return   ResponseEntity.status(HttpStatus.OK).body(list.getContent());
    }



    @PostMapping("/product")
    public ResponseEntity<Product> create(ProductDTO dto) throws IOException {
        log.info("dto : " + dto);
        log.info("file : " + dto.getFile());
//        log.info("fileName : " + fileName);
        // 파일 업로드
        String fileName = dto.getFile().getOriginalFilename();
        //UUID 중복방지
        String uuid = UUID.randomUUID().toString();
        String saveName = uploadPath + File.separator + "product" + File.separator + uuid + "_" + fileName;
        Path savePath = Paths.get(saveName);
        dto.getFile().transferTo(savePath); // 파일업로드 실제로 일어나고 있음


        // product vo 값들 담아서 요청
        Product vo = new Product();
        vo.setProdName(dto.getProdName());
        vo.setPrice(dto.getPrice());
        vo.setProdPhoto(saveName);
        Category category = new Category(); // cateCode를 가져오기 위해서는 Category 객체를 한번 거쳐야함, Product 에서 시작했기 때문
        category.setCateCode(dto.getCateCode());
        vo.setCategory(category);


        Product result = service.create(vo);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(vo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @PutMapping("/product")
    public ResponseEntity<Product> update(ProductDTO dto) throws IOException {

        Product vo = new Product();
        vo.setProdCode(dto.getProdCode());
        vo.setPrice(dto.getPrice());
        vo.setProdName(dto.getProdName());
        Category category = new Category();
        category.setCateCode(dto.getCateCode());
        vo.setCategory(category);
        log.info("file : " + dto.getFile().getOriginalFilename());

        // 기존 데이터를 가져와야 하는 상황 전역으로 빼서 편하게 처리
        Product prev = service.view(dto.getProdCode());

        if(dto.getFile().isEmpty()){
            // 만약 새로운 사진이 없는 경우 -> 기존 사진을 그대로 vo로 담아내기
            vo.setProdPhoto(prev.getProdPhoto());
        } else {

            // 기존 사진을 삭제하고, 새로운 사진을 추가
            File file = new File(prev.getProdPhoto());
            file.delete();

            // 파일 업로드
            String fileName = dto.getFile().getOriginalFilename();
            //UUID
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + "product" + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            dto.getFile().transferTo(savePath); // 파일업로드 실제로 일어나고 있음

            vo.setProdPhoto(saveName);
        }
        Product result = service.update(vo);
        // 삼항연산자
        return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(vo) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/product/{code}")
    public ResponseEntity<Product> delete(@PathVariable(name = "code") int code) {
        Product prev = service.view(code); //  기존의 사진 정보를 가져오기 위해서 입력받은 코드를 통해 해당 상품정보를 가져옴
        File file = new File(prev.getProdPhoto());// 그 상품정보의 사진을 가져옴
        file.delete(); // 그 사진 삭제!! **원래는 기존 사진이 null이라면 이 로직은 실행되면 안됨.**
        Product result = service.delete(code); // 상품 정보도 삭제
        return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 상품 1개 조회
    @GetMapping("/product/{code}")
    public ResponseEntity<Product> view(@PathVariable(name = "code") int code) {
        Product vo = service.view(code);
        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }

    // 상품 댓글추가
    @PostMapping("/product/comment")
    public ResponseEntity createComment(@RequestBody ProductComment vo){

        // 시큐리티에 담은 로그인한 사용자 정보 가져오기
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();

        if(principal instanceof  User){
            User user =(User) principal;
            vo.setUser(user);
            return ResponseEntity.ok(comment.create(vo)); // 댓글추가
        }

        log.info("vo : " + vo);
        return ResponseEntity.badRequest().build();
    }
    // 상품 1개에 따른 댓글 조회 -> 로그인 상관없이 모두에게 보여줘야 함.
    @GetMapping("/public/product/{prodCode}/comment")
    public ResponseEntity<List<ProductCommentDTO>> viewComment(@PathVariable(name = "prodCode")int prodCode){
        List<ProductComment> topList = comment.getTopLevelComments(prodCode);
        List<ProductCommentDTO> response = new ArrayList<>();

        for(ProductComment top : topList){
            List<ProductComment> replies = comment.getRepliesComments(top.getProComCode(), prodCode); // 각각의 상위의 하위 댓글들
            List<ProductCommentDTO> repliesDTO = new ArrayList<>();
            for(ProductComment reply : replies){
                ProductCommentDTO dto = ProductCommentDTO.builder()
                        .prodCode(reply.getProdCode())
                        .proComCode(reply.getProComCode())
                        .proComeDesc(reply.getProComDesc())
                        .proComDate(reply.getProComDate())
                        .user(UserDTO.builder()
                                .id(reply.getUser().getId())
                                .name(reply.getUser().getName())
                                .build())
                        .build();
                repliesDTO.add(dto);
            }

            ProductCommentDTO dto = ProductCommentDTO.builder()
                    .prodCode(top.getProdCode())
                    .proComCode(top.getProComCode())
                    .proComeDesc(top.getProComDesc())
                    .proComDate(top.getProComDate())
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
