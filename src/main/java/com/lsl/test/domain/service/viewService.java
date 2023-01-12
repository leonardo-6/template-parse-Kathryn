package com.lsl.test.domain.service;

import com.alibaba.fastjson2.JSONObject;
import com.lsl.annotation.HibernateSession;
import com.lsl.test.domain.service.branch.viewServiceBranch;
import com.lsl.test.domain.pojo.sql.FreemarkerSqlBean;
import com.lsl.test.infrastructure.porepository.DemoFtl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 视图信息 - 基础设施 持久层PO仓库
 * describe infrastructure层 真正操作数据库
 * @author LuShuL 12304
 * @date 2022/11/3 1:08
 * @version V1.0
 */
@Slf4j
@Repository
@HibernateSession
@Setter
public  class viewService {

    /***
     * 动态代理的接口
     */
    @Autowired
    private DemoFtl demoFtl;


    /****
     * 这相当于分支链
     */
    @Autowired
    private viewServiceBranch viewBranch;


    /*****
     * 1. 解析sql 语句
     * @return
     * @throws Exception
     */
    public String parseSql() {
        // 1. 封装入参
        Map map= viewBranch.packageParameters();

        // 2. 执行对应的模板方法 -- 返回解析的sql语句
        String sql = demoFtl.typesParseSql(map);

        // 3. 返回sql语句结果
        return sql;
    }

    /****
     * 2. 执行sql语句
     * @return
     */
    public Object executeSql(){
        // 1. 封装入参
        Map map= viewBranch.packageParameters();

        // 2. 执行对应的模板方法 -- 执行sql语句
        List result = demoFtl.types(map);

        // 3. 返回结果
        return result;
    }


    /*****
     * 3/ template应用的整体链路
     * @param bean
     * @return
     * @throws Exception
     */
    public String getSql(FreemarkerSqlBean bean) throws Exception {
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(freemarker.template.Configuration.VERSION_2_3_30);

        // 第二步：设置模板文件所在的路径。
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("/freemarker/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(rootPath));

        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");

        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("IndicatorProdSql.sql");

        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。这里由方法输入

        // 第六步：创建一个Writer对象，一般创建FileWriter对象，指定生成的文件名。也可以将生成的文本放入内存中
        //Writer out = new FileWriter(new File("E:\\wyt01web\\src\\main\\resources\\table_ddl_create.sql"));
        String sql = null;
        try (Writer out = new StringWriter();) {
            // 第七步：调用模板对象的process方法输出文件。
            template.process(bean, out);
            sql = ((StringWriter) out).getBuffer().toString();
        } catch (Exception e) {
            log.error("请求信息{},异常信息：", JSONObject.toJSONString(bean),e);
        }
        return sql;
    }

    /****
     * 封装 ModelAndView 返回结果
     * @param o 结果
     * @return
     */
    public ModelAndView packageMd(Object o){
        ModelAndView mv = new ModelAndView();

        /*** 相当于request.getRequestDispatcher("welcome.jsp").forward(request,response); */
        mv.setViewName("views/excuteResult");

        /** 相当于request.setAttribute("name", "张三"); */
        mv.addObject("result",o );

        return mv;
    }

}
