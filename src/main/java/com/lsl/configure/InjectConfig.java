package com.lsl.configure;

import com.lsl.annotation.NextMapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 1. 动态注入的 配置类
 * @Description: for 动态注入
 * @Author: lsl
 * @Data:2023-01-10 16:51
 * @Projectname: Kathryn
 * @Filename: InjectConfig
 */
@Configuration
@NextMapperScan({"com.lsl.test.infrastructure.porepository"})
public class InjectConfig {

}
