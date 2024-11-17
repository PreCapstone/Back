package com.springboot.apiserver.user.images.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="user_sample_images")
@Getter
@Setter
@NoArgsConstructor
public class UserSampleImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSampleImageId;

    @Column(name="USER_ID",nullable = false)
    private String userId;

    @Column(name="USER_SAMPLE_IMAGE",nullable = false)
    private String userSampleImage;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;
}
