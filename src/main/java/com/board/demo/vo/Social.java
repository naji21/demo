package com.board.demo.vo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
@Builder
public class Social extends BaseTimeEntity implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_no", nullable = false)
	private long socialNo;
	
	@Column(name = "provider", nullable = false)
	private String provider;
	
	@Column(name = "access_token", nullable = false)
	private String accessToken;
	
	@Column(name = "refresh_token", nullable = false)
	private String refreshToken;
	
	@Column(name = "expired_at", nullable = false)
	private Date expiredAt;
}
