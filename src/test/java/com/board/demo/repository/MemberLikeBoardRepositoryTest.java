package com.board.demo.repository;

import com.board.demo.vo.Board;
import com.board.demo.vo.Member;
import com.board.demo.vo.MemberLikeBoard;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberLikeBoardRepositoryTest {

    @Autowired
    private MemberLikeBoardRepository memberLikeBoardRepository;

    @Test
    @Transactional
    public void create() {  // click to like
        long boardId = 30L;
        long memberId = 20L;

        MemberLikeBoard mlb = MemberLikeBoard.builder()
                .member(Member.builder().memberId(memberId).build())
                .board(Board.builder().boardId(boardId).build())
                .build();

        mlb = memberLikeBoardRepository.save(mlb);
        Assert.assertNotEquals(mlb.getMlbId(), 0L);
    }

    @Test
    @Transactional
    public void delete() {  // cancel to like
        long boardId = 11L;
        long memberId = 20L;

        Optional<MemberLikeBoard> mlb = memberLikeBoardRepository.findByBoardAndMember(Board.builder().boardId(boardId).build(), Member.builder().memberId(memberId).build());
        mlb.ifPresent(memberLikeBoardRepository::delete);


        Assert.assertFalse(memberLikeBoardRepository.findByBoardAndMember(Board.builder().boardId(boardId).build(), Member.builder().memberId(memberId).build()).isPresent());
    }
}
