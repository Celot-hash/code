package com.zjq.javacode.service;

import com.zjq.javacode.common.RunResults;
import com.zjq.javacode.classloader.MyClassLoader;
import com.zjq.javacode.util.Resource;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class IndexService {

    //执行代码，返回执行结果
    public RunResults runCode(String code) throws Exception {
        String stdout = null;
        code = "import java.io.*;\n" +
                "import java.lang.*;\n" +
                "import java.math.*;\n" +
                "import java.net.*;\n" +
                "import java.nio.*;\n" +
                "import java.text.*;\n" +
                "import java.time.*;\n" +
                "import java.util.*;\n" + code;
        String filePath = Resource.getUrl("classes") + "/Test.java";
        List<String> errorMessage = compilerJavaCode(filePath, code);
        //代表编译成功
        if (errorMessage.isEmpty()) {
            Class<?> myClass = new MyClassLoader().loadClass(filePath.replace(".java", ".class"));
            if (myClass != null) {
                //给输出流加锁，防止多线程情况下的并发问题
                synchronized (System.out) {
                    //原先的输出流
                    PrintStream
                            oldStream = System.out;
                    //创建获取控制台信息的输出流
                    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
                    PrintStream printStream = new PrintStream(byteStream);
                    //截取控制台输出信息
                    System.setOut(printStream);
                    Method method = myClass.getMethod("main", new Class[]{String[].class});
                    method.invoke(null, new String[]{null});
                    //将截取的信息转换成字符串
                    stdout = byteStream.toString();
                    //截取完毕，将输出信息返回给控制台
                    System.setOut(oldStream);
                }
            }
        }

        return RunResults.builder()
                .compiled(errorMessage.size() == 0)
                .errorMessage(errorMessage)
                .stdout(stdout == null ? "" : stdout)
                .build();
    }


     //保存代码字符串为.java文件，然后通过JavaCompiler编译为.class文件
    private List<String> compilerJavaCode(String filePath, String code) throws Exception {
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] bytes = code.getBytes();
            outputStream.write(bytes);
            outputStream.flush();
            // 使用JavaCompiler编译java文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            // 建立DiagnosticCollector对象
            DiagnosticCollector<JavaFileObject> diagnosticCollectors = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(diagnosticCollectors, null, null);
            //建立用于保存被编译文件名的对象
            //每个文件被保存在一个继承JavaFileObject的类中
            Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(filePath);
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, diagnosticCollectors, null, null, fileObjects);
            Boolean call = task.call();
            fileManager.close();

            if (call) {
                return Collections.emptyList();
            }
            List<String> errorMessage = new ArrayList<>();
            for (Diagnostic diagnostic : diagnosticCollectors.getDiagnostics()) {
                String message = String.format("第 %s 行: 错误原因: %s", diagnostic.getLineNumber()-8, diagnostic.getMessage(null));
                errorMessage.add(message);
            }
            errorMessage.add(errorMessage.size() + " errors");
            return errorMessage;
        } catch (Exception e) {
            throw e;
        }
    }

}
