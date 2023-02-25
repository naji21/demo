package com.board.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.board.demo.vo.Board;

import static com.board.demo.vo.QBoard.board;
import static com.board.demo.vo.QReply.reply;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardRepositorySupport {
	
	private final JPAQueryFactory queryFactory;
	
	public List<Board> getBoardListByLikeTop5(){
		return queryFactory.selectFrom(board).innerJoin(board.replyList, reply).fetchJoin()
				.orderBy(board.replyList.size().desc()).limit(5).fetch();
	}
}
