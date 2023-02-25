package com.board.demo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.board.demo.vo.Member;
import com.board.demo.vo.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ReplyDto implements Serializable{
    private Long replyId;

    private int level;
    
    private Long parent;

    private String content;

    private LocalDateTime date;
    
    private Member member;
    
    public ReplyDto(Reply reply) {
    	this.replyId = reply.getReplyId();
    	this.parent = reply.getParent();
    	this.content = reply.getContent();
    	this.date = reply.getDate();
    	this.member = reply.getMember();
    }
}
