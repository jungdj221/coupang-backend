package com.kh.coupang.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class ReviewComment {

    @Id
    @Column(name = "revi_com_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviComCode;

    @Column(name = "revi_com_desc")
    private String reviComDesc;

    @Column(name = "revi_com_date")
    private Date reviComDate;

    @Column(name = "revi_com_parent")
    private int ReviComParent;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "revi_com_parent", referencedColumnName = "revi_com_code", insertable = false, updatable = false)
    private ReviewComment parent; // 셀프조인을 위해서 code = parent_code

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "revi_code")
    private int reviCode;
}
