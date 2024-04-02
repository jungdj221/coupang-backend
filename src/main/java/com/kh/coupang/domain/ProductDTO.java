package com.kh.coupang.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

        private int prodCode;
        private String prodName;
        private int price;
        private MultipartFile file;
        private int cateCode;
}
