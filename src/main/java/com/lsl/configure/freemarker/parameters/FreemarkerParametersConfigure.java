package com.lsl.configure.freemarker.parameters;

import lombok.Data;

/**
 * @Description: freemark配置的用户主要参数
 * @Author: lsl
 * @Data:2022-11-30 14:43
 * @Projectname: ehrview
 * @Filename: DesenConfigure
 */

@Data
public class FreemarkerParametersConfigure{

    /** 模板文件所在的路径 */
    private String freemarkerResourceName;

    /** 配置文件名 */
    private String[] configFileNames;

    /** 自定义标签 */
    private String[] customTags;

    /** 基础标签 路径 */
    private String baseTag;

    /** 其他工具1 校验工具 路径 */
    private String checkUtil;


}
