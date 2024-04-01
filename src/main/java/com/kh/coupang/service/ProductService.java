package com.kh.coupang.service;

import com.kh.coupang.domain.Product;
import com.kh.coupang.repo.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO dao;
    public List<Product> viewAll(){
        return dao.findAll();
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

    public List<Product> viewCategory(int code) {
    return dao.findByCaeCode(code);
    }
}
