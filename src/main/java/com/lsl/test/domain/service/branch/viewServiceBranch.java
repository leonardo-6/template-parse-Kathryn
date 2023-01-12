package com.lsl.test.domain.service.branch;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-12 18:52
 * @Projectname: Kathryn
 * @Filename: viewServiceBranch
 */
@Service
public class viewServiceBranch {

    public Map packageParameters(){

        Map<String, Object> customerParameters = new HashMap<>();

        customerParameters.put("viewName","测试");
        customerParameters.put("createdBy","12314");

        String[] strings = {"10", "8"};
        customerParameters.put("viewType",strings);

        return customerParameters;
    }
}
