package com.kh.coupang.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(name="cate_code")
    private int cateCode;

    @Column(name="cate_icon")
    private String cateIcon;

    @Column(name="cate_name")
    private String cateName;

    @Column(name = "cate_url")
    private String cateUrl;

    @Column(name = "parent_code")
    private Integer parentCode;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_code",referencedColumnName = "cate_code", insertable = false, updatable = false)
    private Category category;
}
