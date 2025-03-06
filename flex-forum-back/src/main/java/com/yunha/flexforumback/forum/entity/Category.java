package com.yunha.flexforumback.forum.entity;

import com.yunha.flexforumback.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class Category {
    @Id
    @Column(name = "category_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryCode;
    @Column(name = "name")
    private String name;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "enable")
    private boolean enable;
    @OneToOne
    @JoinColumn(name = "create_user")
    private User createUser;
}
