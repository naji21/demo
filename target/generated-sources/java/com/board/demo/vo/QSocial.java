package com.board.demo.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSocial is a Querydsl query type for Social
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSocial extends EntityPathBase<Social> {

    private static final long serialVersionUID = 863821932L;

    public static final QSocial social = new QSocial("social");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath accessToken = createString("accessToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.sql.Date> expiredAt = createDate("expiredAt", java.sql.Date.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath provider = createString("provider");

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Long> socialNo = createNumber("socialNo", Long.class);

    public QSocial(String variable) {
        super(Social.class, forVariable(variable));
    }

    public QSocial(Path<? extends Social> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSocial(PathMetadata metadata) {
        super(Social.class, metadata);
    }

}

