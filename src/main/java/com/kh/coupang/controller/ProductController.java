package com.kh.coupang.controller;

import com.kh.coupang.domain.Product;
import com.kh.coupang.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j  // syso 같은 역할
@RestController
@RequestMapping("/api/*")
public class ProductController {

    @Autowired
    private ProductService service;
    // 메서드명은 service 랑 동일

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
    public ResponseEntity<Product> create(@RequestBody Product vo) {
        Product result = service.create(vo);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(vo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @PutMapping("/product")
    public ResponseEntity<Product> update(@RequestBody Product vo) {
        Product result = service.update(vo);
        // 삼항연산자
        return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(vo) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/product/{code}")
    public ResponseEntity<Product> delete(@PathVariable(name = "code") int code) {
        Product result = service.delete(code);
        return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
