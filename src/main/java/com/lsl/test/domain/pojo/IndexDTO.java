package com.lsl.test.domain.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2022-12-05 20:28
 * @Projectname: 11_freemarker
 * @Filename: IndexDTO
 */
@Data
public class IndexDTO implements Serializable {

    private String title;

    private String contextPath;
}
