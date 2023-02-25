package com.board.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude="board, member")
public class Reply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private Long replyId;

    @Column(name = "parent", nullable = false)
    private Long parent;

//    @Column(name = "sorts", nullable = false)
//    private Long sorts;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date")
    @CreatedDate
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="member_id")
    private Member member;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="board_id")
    private Board board;

}
