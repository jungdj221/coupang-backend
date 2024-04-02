package com.kh.coupang.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name="review_image")   //db상의 테이블명에 _ 언더바가 있을경우 또 명시를 해줘야함
public class ReviewImage {

    @Id
    @Column(name="revi_img_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviImgCode;

    @Column(name = "revi_url")
    private String reviUrl;

    @ManyToOne
    @JoinColumn(name="revi_code")
    @JsonIgnore
    private Review review;
}
