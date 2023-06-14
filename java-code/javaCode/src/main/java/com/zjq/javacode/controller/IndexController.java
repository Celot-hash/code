package com.zjq.javacode.controller;

import com.zjq.javacode.common.BaseResponse;
import com.zjq.javacode.common.CodeResponse;
import com.zjq.javacode.common.RunResults;
import com.zjq.javacode.service.IndexService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;



@RestController
@Validated
public class IndexController {

    @Resource
    private IndexService indexService;

    @PostMapping("run_code")
    public BaseResponse runCode(String code) throws Exception {
        RunResults runResults = indexService.runCode(code);
        return new CodeResponse(runResults);
    }

}
