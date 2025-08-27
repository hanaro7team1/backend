package com.sido.backend.stay.entity;

import com.sido.backend.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "stay_image")
public class StayImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgName; // 원본 파일명

    private String saveName; // 저장된 파일명

    private String saveUrl; // 저장된 파일 경로

    private String s3Key; // S3 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stay_id")
    private Stay stay;
}
