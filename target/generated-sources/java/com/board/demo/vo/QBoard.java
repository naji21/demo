package com.board.demo.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -126383609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final QCategory category;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final ListPath<MemberLikeBoard, QMemberLikeBoard> likeList = this.<MemberLikeBoard, QMemberLikeBoard>createList("likeList", MemberLikeBoard.class, QMemberLikeBoard.class, PathInits.DIRECT2);

    public final QMember member;

    public final ListPath<Reply, QReply> replyList = this.<Reply, QReply>createList("replyList", Reply.class, QReply.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final NumberPath<Long> views = createNumber("views", Long.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

