package com.kh.coupang.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCommentDTO {

    private int proComCode;
    private String proComeDesc;
    private Date proComDate;
    private int prodCode;
    private UserDTO user;
    private List<ProductCommentDTO> replies = new ArrayList<>();
}
