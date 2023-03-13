package com.board.demo.dto;

import java.io.Serializable;

import javax.persistence.Column;

import com.board.demo.vo.Member;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class UserDto extends Member{
	private String provider;
}
