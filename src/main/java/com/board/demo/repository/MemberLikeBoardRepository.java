package com.board.demo.repository;

import com.board.demo.vo.Board;
import com.board.demo.vo.Member;
import com.board.demo.vo.MemberLikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLikeBoardRepository extends JpaRepository<MemberLikeBoard, Long> {
    Optional<MemberLikeBoard> findByBoardAndMember(Board board, Member member);
}
