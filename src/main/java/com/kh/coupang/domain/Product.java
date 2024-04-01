package com.kh.coupang.domain;

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
public class Product {

    @Id
    @Column(name="prod_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //중요!!! primary key가 입력 동시에 동기화 업데이트
    private int prodCode; // prod_code
    @Column(name="prod_name")
    private String prodName; // prod_name
    @Column
    private int price;

    @ManyToOne //현위치에서 가져오는 객체와의 관계
    @JoinColumn(name = "cate_code") // 서로 맞닿는 컬럼 foreginkey
    private Category category;
}
