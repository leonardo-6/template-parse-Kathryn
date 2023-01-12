package com.lsl.configure.freemarker.tags;

import com.lsl.configure.freemarker.BaseTag;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义的freemarker标签
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 * @modify by zhyd 2018-09-20
 *      调整实现，所有自定义标签只需继承BaseTag后通过构造函数将自定义标签类的className传递给父类即可。
 *      增加标签时，只需要添加相关的方法即可，默认自定义标签的method就是自定义方法的函数名。
 *      例如：<@zhydTag method="types" ...></@zhydTag>就对应 {{@link #(Map)}}方法
 */
@Component
public class JavaTags extends BaseTag {

    public JavaTags() {
        super(JavaTags.class.getName());
    }

}
