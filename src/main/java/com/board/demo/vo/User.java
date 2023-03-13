package com.board.demo.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column 
    private String picture;
    
    @Column 
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String picture, String provider, Role role){
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
        this.role = role;
    }

    public User update(String name, String picture, String provider){
        this.name = name;
        this.picture = picture;
        this.provider = provider;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}