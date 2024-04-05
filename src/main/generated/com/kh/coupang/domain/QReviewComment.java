package com.kh.coupang.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewComment is a Querydsl query type for ReviewComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewComment extends EntityPathBase<ReviewComment> {

    private static final long serialVersionUID = 1983373628L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewComment reviewComment = new QReviewComment("reviewComment");

    public final QReviewComment parent;

    public final NumberPath<Integer> reviCode = createNumber("reviCode", Integer.class);

    public final NumberPath<Integer> reviComCode = createNumber("reviComCode", Integer.class);

    public final DateTimePath<java.util.Date> reviComDate = createDateTime("reviComDate", java.util.Date.class);

    public final StringPath reviComDesc = createString("reviComDesc");

    public final NumberPath<Integer> ReviComParent = createNumber("ReviComParent", Integer.class);

    public final QUser user;

    public QReviewComment(String variable) {
        this(ReviewComment.class, forVariable(variable), INITS);
    }

    public QReviewComment(Path<? extends ReviewComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewComment(PathMetadata metadata, PathInits inits) {
        this(ReviewComment.class, metadata, inits);
    }

    public QReviewComment(Class<? extends ReviewComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QReviewComment(forProperty("parent"), inits.get("parent")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

