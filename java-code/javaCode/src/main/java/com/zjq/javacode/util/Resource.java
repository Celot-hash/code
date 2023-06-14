package com.zjq.javacode.util;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;


public class Resource {

    //获取 resources 文件夹下的绝对路径
    public static String getUrl(String dirName) {
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:" + dirName).getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }

}
