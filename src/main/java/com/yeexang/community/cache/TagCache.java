package com.yeexang.community.cache;

import com.yeexang.community.dto.TagDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    private static List<String> programTags = new ArrayList<String>() {
        {
            this.add("JavaScirpt");
            this.add("Java");
            this.add("C");
            this.add("C++");
            this.add("PHP");
        }
    };
    private static List<String> frameworkTags = new ArrayList<String>() {
        {
            this.add("django");
            this.add("spring");
            this.add("express");
        }
    };

    /**
     * 提供标签选项信息
     * @return
     */
    public static List<TagDTO> getTags() {

        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategory("开发语言");
        program.setTags(programTags);
        TagDTO framework = new TagDTO();
        framework.setCategory("平台框架");
        framework.setTags(frameworkTags);
        tagDTOList.add(program);
        tagDTOList.add(framework);
        return tagDTOList;
    }

    public static String filterInvaild(String tags) {

        String[] spilt = StringUtils.split(tags, ",");

        if (spilt == null) {
            spilt = new String[]{tags};
        }

        List<TagDTO> tagDTOS = getTags();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        for (String tag : spilt) {
            if (!tagList.contains(tag)) {
                return tag;
            }
        }
        return null;
    }
}
