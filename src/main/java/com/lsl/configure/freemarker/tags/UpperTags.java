package com.lsl.configure.freemarker.tags;

import com.lsl.configure.freemarker.BaseTag;
import freemarker.core.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * 参考的官网写的一个测试自定义标签
 * 自定义的freemarker标签
 *
 * @author lsl
 * @version 1.0
 * @since 1.0
 *      调整实现，所有自定义标签只需继承BaseTag后通过构造函数将自定义标签类的className传递给父类即可。
 *      增加标签时，只需要添加相关的方法即可，默认自定义标签的method就是自定义方法的函数名。
 *      例如：<@UpperTags method="types" ...></@UpperTags>就对应 {{@link #(Map)}}方法
 */
@Component
public class UpperTags extends BaseTag {

    public UpperTags() {
        super(UpperTags.class.getName());
    }


    @Override
    public Writer environmentHandle(Environment environment){
        return new UpperCaseFilterWriter(environment.getOut());
    }

    /**
     * A {@link Writer} that transforms the character stream to upper case
     * and forwards it to another {@link Writer}.
     */
    private static class UpperCaseFilterWriter extends Writer {

        private final Writer out;

        UpperCaseFilterWriter (Writer out) { this.out = out; }

        @Override
        public void write(char[] cbuf, int off, int len)  throws IOException {
            char[] transformedCbuf = new char[len];

            for (int i = 0; i < len; i++) {
                transformedCbuf[i] = Character.toUpperCase(cbuf[i + off]);
            }

            out.write(transformedCbuf);
        }

        @Override
        public void flush() throws IOException { out.flush(); }

        @Override
        public void close() throws IOException { out.close(); }

    }
}
