package com.board.demo.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberLikeBoard is a Querydsl query type for MemberLikeBoard
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMemberLikeBoard extends EntityPathBase<MemberLikeBoard> {

    private static final long serialVersionUID = -254722570L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberLikeBoard memberLikeBoard = new QMemberLikeBoard("memberLikeBoard");

    public final QBoard board;

    public final QMember member;

    public final NumberPath<Long> mlbId = createNumber("mlbId", Long.class);

    public QMemberLikeBoard(String variable) {
        this(MemberLikeBoard.class, forVariable(variable), INITS);
    }

    public QMemberLikeBoard(Path<? extends MemberLikeBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberLikeBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberLikeBoard(PathMetadata metadata, PathInits inits) {
        this(MemberLikeBoard.class, metadata, inits);
    }

    public QMemberLikeBoard(Class<? extends MemberLikeBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

