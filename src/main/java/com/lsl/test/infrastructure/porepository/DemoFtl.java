package com.lsl.test.infrastructure.porepository;

import com.lsl.annotation.NextSelect;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-10 15:55
 * @Projectname: Kathryn
 * @Filename: UserMapper
 */
public interface DemoFtl {
    @NextSelect("select * from lsl")
    List  types(Map map);

    String typesParseSql(Map map);
}
