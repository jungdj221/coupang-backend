package com.kh.coupang.service;

import com.kh.coupang.domain.Category;
import com.kh.coupang.domain.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QCategory qCategory = QCategory.category1;
//     상위 카테고리만 가져오기
    /*
    * SELECT * FROM category WHERE parent_code IS NULL
    * */
    public List<Category> getTopLevelCategory(){
        return queryFactory.selectFrom(qCategory)
                .where(qCategory.parentCode.isNull())
                .fetch();
    }


    // 하위 카테고리만 가져오기
    /*
     * SELECT * FROM category WHERE parent_code = ?
     * */
    public  List<Category> getBottomLevelCategory(int parent){
        return queryFactory.selectFrom(qCategory)
                .where(qCategory.parentCode.eq(parent))
                .fetch();
    }


}
