package com.lsl.dynamic.compiler.classload;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-04 13:36
 * @Projectname: Kathryn
 * @Filename: MyClassLoader
 */
public class NextClassLoader extends URLClassLoader {
    public NextClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}


