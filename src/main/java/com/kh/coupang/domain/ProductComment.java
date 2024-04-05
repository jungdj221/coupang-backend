package com.kh.coupang.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class ProductComment {

    @Id
    @Column(name = "pro_com_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int proComCode;

    @Column(name = "pro_com_desc")
    private String proComDesc;

    @Column(name = "pro_com_date")
    private Date proComDate;

    @Column(name = "pro_com_parent")
    private int proComParent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pro_com_parent" ,referencedColumnName = "pro_com_code", insertable = false, updatable = false)
    private ProductComment parent;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "prod_code")
    private int prodCode;
}
