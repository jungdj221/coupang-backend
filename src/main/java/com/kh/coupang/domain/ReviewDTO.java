package com.kh.coupang.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private int reviCode;
    private String id;
    private int prodCode;
    private String reviTitle;
    private String reviDesc;
    private int rating;
    private List<String> Images;
    private List<MultipartFile> files;
}
