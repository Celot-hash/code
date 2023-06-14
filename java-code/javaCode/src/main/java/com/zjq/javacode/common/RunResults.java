package com.zjq.javacode.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunResults {

    //编译是否成功
    private boolean compiled;

    //编译失败此字段值为错误信息
    private List<String> errorMessage;

    //运行输出信息
    private String stdout;

}
