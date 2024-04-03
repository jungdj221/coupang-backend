package com.kh.coupang.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 2011379337L;

    public static final QCategory category = new QCategory("category");

    public final NumberPath<Integer> cateCode = createNumber("cateCode", Integer.class);

    public final StringPath cateIcon = createString("cateIcon");

    public final StringPath cateName = createString("cateName");

    public final StringPath cateUrl = createString("cateUrl");

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

