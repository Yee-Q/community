package com.yeexang.community.cache;

import com.yeexang.community.dto.TagDTO;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    /**
     * 提供标签选项信息
     *
     * @return
     */
    public static List<TagDTO> getTags() {

        List<TagDTO> tagDTOList = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategory("开发语言");
        program.setTags(Arrays.asList("JavaScirpt", "Java", "C", "C++", "PHP"));
        TagDTO framework = new TagDTO();
        framework.setCategory("平台框架");
        framework.setTags(Arrays.asList("django", "spring", "express"));
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
