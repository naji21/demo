package com.board.demo.filter;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.board.demo.vo.Member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberContext extends User {
  private final Member member;

  public MemberContext(Member member, List<GrantedAuthority> roles) {
    super(member.getId(), member.getPwd(), roles);
    this.member = member;
  }
}