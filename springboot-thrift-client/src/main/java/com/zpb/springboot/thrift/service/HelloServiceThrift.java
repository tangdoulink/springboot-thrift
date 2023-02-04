package com.zpb.springboot.thrift.service;

import api.HelloService;
import com.zpb.springboot.thrift.config.ThriftClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author       pengbo.zhao
 * @description  hello-service-thrift
 * @createDate   2022/1/5 14:40
 * @updateDate   2022/1/5 14:40
 * @version      1.0
 */
@Service
public class HelloServiceThrift {

    @Resource
    private ThriftClient thriftClient;

    public String sayHello(){
        try {
            thriftClient.open();
            HelloService.Client helloServiceClient = thriftClient.getHelloServiceClient();
            return helloServiceClient.sayHello("I'm springboot-client");
        } catch (Exception e){
            e.printStackTrace();
            return "request error";
        }
    }

}
