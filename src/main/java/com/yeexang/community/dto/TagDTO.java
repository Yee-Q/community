package com.yeexang.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {

    private String category;
    private List<String> tags;
}
