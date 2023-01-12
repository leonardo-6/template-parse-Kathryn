package com.lsl.test.domain.pojo.sql;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2022-12-06 19:49
 * @Projectname: Kathryn
 * @Filename: FreemarkerSqlBean
 */
@Data
public class FreemarkerSqlBean {
    public String express;
    public String modelName;
    public List<FreemarkerSqlJoin> joinList;
    public List<FreemarkerSqlWhere> whereList;
    public List<String> groupList;
    public List<String> orderList;

}
