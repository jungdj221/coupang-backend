package com.kh.coupang.service;

import com.kh.coupang.domain.Product;
import com.kh.coupang.repo.ProductDAO;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO dao;
    public Page<Product> viewAll(Pageable pageable, BooleanBuilder builder){
        return dao.findAll(builder, pageable);
    }
    public Product view(int code){

        return dao.findById(code).orElse(null);
    }

    public Product create(Product vo){

        return dao.save(vo);
    }

    public Product update(Product vo){
        if(dao.existsById(vo.getProdCode())){
            return dao.save(vo);
        } return null;

    }

    public Product delete(int code){
        Product target = dao.findById(code).orElse(null);
        if(target!=null){
            dao.deleteById(code);
//            dao.delete(target); 둘다 똑같은 기능
        }
        return target;
    }

    public Page<Product> viewCategory(int code, Pageable pageable) {
    return dao.findByCaeCode(code,pageable);
    }
}
