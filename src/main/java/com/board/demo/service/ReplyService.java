package com.board.demo.service;

import com.board.demo.vo.Member;
import com.board.demo.vo.Reply;
import org.springframework.data.domain.Page;

public interface ReplyService {

    boolean writeReply(long boardId, long parent, String content, Member member);

    boolean deleteReply(long replyId, long parent, Member member);

    Page<Reply> getListByMemberId(long memberId, int page, int size);
    
    int getCountByMemberId(long memberId);
}
