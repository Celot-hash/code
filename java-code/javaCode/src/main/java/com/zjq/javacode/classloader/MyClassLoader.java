package com.zjq.javacode.classloader;

import java.io.FileInputStream;

//类加载器
public class MyClassLoader extends ClassLoader {

    //根据文件地址加载类
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try (FileInputStream inputStream = new FileInputStream(name)) {
            if (inputStream == null) {
                return super.findClass(name);
            }
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return defineClass(null, bytes, 0, bytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
    }
}
