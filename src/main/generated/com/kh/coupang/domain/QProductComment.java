package com.kh.coupang.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductComment is a Querydsl query type for ProductComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductComment extends EntityPathBase<ProductComment> {

    private static final long serialVersionUID = 143655867L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductComment productComment = new QProductComment("productComment");

    public final QProductComment parent;

    public final NumberPath<Integer> proComCode = createNumber("proComCode", Integer.class);

    public final DateTimePath<java.util.Date> proComDate = createDateTime("proComDate", java.util.Date.class);

    public final StringPath proComDesc = createString("proComDesc");

    public final NumberPath<Integer> proComParent = createNumber("proComParent", Integer.class);

    public final NumberPath<Integer> prodCode = createNumber("prodCode", Integer.class);

    public final QUser user;

    public QProductComment(String variable) {
        this(ProductComment.class, forVariable(variable), INITS);
    }

    public QProductComment(Path<? extends ProductComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductComment(PathMetadata metadata, PathInits inits) {
        this(ProductComment.class, metadata, inits);
    }

    public QProductComment(Class<? extends ProductComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QProductComment(forProperty("parent"), inits.get("parent")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

