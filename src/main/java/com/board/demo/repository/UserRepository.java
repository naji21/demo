package com.board.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.demo.vo.User;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email); // 이미 email을 통해 생성된 사용자인지 체크
}