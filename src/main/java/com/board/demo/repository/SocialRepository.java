package com.board.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.demo.vo.Social;
import com.board.demo.vo.User;

public interface SocialRepository extends JpaRepository<Social,Long> {
    Optional<Social> findBySocialNo(long socialNo);
}