package com.kh.coupang.controller;

import com.kh.coupang.domain.Category;
import com.kh.coupang.domain.Product;
import com.kh.coupang.domain.ProductDTO;
import com.kh.coupang.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j  // syso 같은 역할
@RestController
@RequestMapping("/api/*")
public class ProductController {

    @Autowired
    private ProductService service;
    // 메서드명은 service 랑 동일

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath; // 업로드 경로 역할을 해줌(D:\\upload)


    @GetMapping("/product")                                                     // 이 속성은 받는 것은 필수가 아님
    public ResponseEntity<List<Product>> viewAll(@RequestParam(name="category", required = false) Integer category) {
        log.info("category : " + category);
        List<Product> list = service.viewAll();
        return category==null ?  ResponseEntity.status(HttpStatus.OK).body(list):
                                ResponseEntity.status(HttpStatus.OK).body(service.viewCategory(category));
    }

    @GetMapping("/product/{code}")
    public ResponseEntity<Product> view(@PathVariable(name = "code") int code) {
        Product vo = service.view(code);
        return ResponseEntity.status(HttpStatus.OK).body(vo);
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
}
