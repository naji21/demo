package com.board.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "member_like_board")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@ToString(exclude={"board", "member"}, callSuper = true)
public class MemberLikeBoard implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mlb_id", nullable = false)
    private long mlbId;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
	private Member member;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="board_id")
    private Board board;
}
