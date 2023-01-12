package com.lsl.test.application.controller;

import com.lsl.test.domain.service.viewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2022-12-06 16:42
 * @Projectname: Kathryn
 * @Filename: HibernateTest
 */
@Controller
@Slf4j
public class HibernateTest {

    @Autowired
    private viewService viewService;


    @RequestMapping( "parseSql.do")
    public ModelAndView parseSql() throws Exception {
        log.info("========= 进入parseSql方法");

        String sql = viewService.parseSql();

        return viewService.packageMd(sql);
    }

    @RequestMapping( "executeSql.do")
    public ModelAndView executeSql() throws Exception {
        log.info("进入executeSql 方法");

        Object o = viewService.executeSql();

        return viewService.packageMd(o);
    }



    /**
     * @param request
     * @return ModelView
     * */
    @RequestMapping("/freemarkerIndex")
    public ModelAndView freemarkerIndex(HttpServletRequest request) {
        /*IndexDTO dto = new IndexDTO();
        dto.setTitle("freemarker在web环境中的使用");
        String contextPath = request.getContextPath();
        dto.setContextPath(contextPath);
        request.getSession().setAttribute("website", "http://www.roadjava.com");
        // index: 逻辑视图名
        // indexDTO:root
        return new ModelAndView("indexF", "indexDTO", dto);*/
        return null;
    }

    /****
     * 最早 打算用的万能模板计划 调试
     * @throws Exception
     */
    @RequestMapping( "hibernateXXX.do")
    public void add() throws Exception {
        /*System.out.println("StudentController---add-----");
        FreemarkerSqlBean freemarkerSqlBean = new FreemarkerSqlBean();
        freemarkerSqlBean.setExpress("xxx");
        freemarkerSqlBean.setModelName("table");

        List<FreemarkerSqlJoin> array = new ArrayList<>();
        FreemarkerSqlJoin join = new FreemarkerSqlJoin();
        join.setType(",");
        join.setModelName("table2");
        join.setCriteria("table.a = table.b");
        array.add(join);

        List<FreemarkerSqlWhere> array2 = new ArrayList<>();
        FreemarkerSqlWhere freemarkerSqlWhere = new FreemarkerSqlWhere();
        freemarkerSqlWhere.setType("and");
        freemarkerSqlWhere.setCriteria("table1.c like '%cc%'");
        array2.add(freemarkerSqlWhere);

        List<String> groupList = new ArrayList<>();
        groupList.add("age");
        groupList.add("name");

        List<String> orderList = new ArrayList<>();
        orderList.add("grad");

        freemarkerSqlBean.setJoinList(array);
        freemarkerSqlBean.setWhereList(array2);
        freemarkerSqlBean.setGroupList(groupList);
        freemarkerSqlBean.setOrderList(orderList);
        System.out.println(viewPORepository.getSql(freemarkerSqlBean));*/

    }
}
