package com.zpb.springboot.thrift.controller;

import com.zpb.springboot.thrift.service.HelloServiceThrift;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author       pengbo.zhao
 * @description  hello-controller
 * @createDate   2022/1/5 14:45
 * @updateDate   2022/1/5 14:45
 * @version      1.0
 */
@RestController
@RequestMapping("thrift")
public class HelloController {

    @Resource
    private HelloServiceThrift helloServiceThrift;


    @GetMapping("say")
    public Map<String,Object> sayHello(){
        long star = System.currentTimeMillis();
        HashMap<String, Object> resultMap = new HashMap<>();
        String s = helloServiceThrift.sayHello();
        if(!StringUtils.isEmpty(s)){
            resultMap.put("data",s);
            resultMap.put("message","ok");
            resultMap.put("code",200);
        } else {
            resultMap.put("data","");
            resultMap.put("message","false");
            resultMap.put("code",500);
        }
        System.err.println("rt: " + (System.currentTimeMillis() - star));
        return resultMap;
    }

}
