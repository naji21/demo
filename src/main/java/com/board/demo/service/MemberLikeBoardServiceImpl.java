package com.board.demo.service;

import com.board.demo.repository.MemberLikeBoardRepository;
import com.board.demo.vo.Board;
import com.board.demo.vo.Member;
import com.board.demo.vo.MemberLikeBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberLikeBoardServiceImpl implements MemberLikeBoardService {
    @Autowired
    private MemberLikeBoardRepository memberLikeBoardRepository;


    @Override
    public boolean isLike(long boardId, long memberId) {
        return memberLikeBoardRepository.findByBoardAndMember(Board.builder().boardId(boardId).build(), Member.builder().memberId(memberId).build())
                .isPresent();
    }

    @Override
    public boolean like(long memberId, long boardId) {
        MemberLikeBoard mlb = MemberLikeBoard.builder()
        		.member(Member.builder().memberId(memberId).build())
        		.board(Board.builder().boardId(boardId).build())
                //.memberId(memberId)
                //.boardId(boardId)
                .build();
        try {
            memberLikeBoardRepository.save(mlb);
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
        return true;
    }

    @Override
    public void dislike(long memberId, long boardId) {
        Optional<MemberLikeBoard> mlb = memberLikeBoardRepository.findByBoardAndMember(Board.builder().boardId(boardId).build(), Member.builder().memberId(memberId).build());
        mlb.ifPresent(memberLikeBoardRepository::delete);
    }
}
