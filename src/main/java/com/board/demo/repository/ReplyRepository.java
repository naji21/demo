package com.board.demo.repository;

import com.board.demo.vo.Member;
import com.board.demo.vo.Reply;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>{
    List<Reply> findAllByBoard(long board_id);

    void deleteByReplyIdAndParentAndMember(long replyId, long parent, Member member);

    Optional<Reply> findByReplyIdAndParentAndMember(long replyId, long parent, Member member);

    List<Reply> findAllByParent(long parent);

    Optional<Reply> findByReplyIdAndParent(long replyId, long parent);
    
    Page<Reply> findAllByMember(Member member, Pageable pageRequest);
    
    int countByMember(Member member);
}
