package com.zpb.springboot.thrift.config;

import api.HelloService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author       pengbo.zhao
 * @description  thrift-client
 * @createDate   2022/1/5 14:21
 * @updateDate   2022/1/5 14:21
 * @version      1.0
 */
@Component
@ConditionalOnBean(ThriftProperties.class)
public class ThriftClient {

    private HelloService.Client helloServiceClient;

    private TTransport tTransport;

    @Resource
    private ThriftProperties thriftProperties;

    /**
     * 初始化 thrift-client
     * @throws TTransportException ex
     */
    @PostConstruct
    public void initClient() throws TTransportException {

        // 设置socket连接
        TSocket tSocket = new TSocket(thriftProperties.getHost(), thriftProperties.getPort());

        // 传输方式-以frame为单位
        tTransport = new TFramedTransport(tSocket,600);

        // 传输协议-二进制
        TProtocol tProtocol = new TBinaryProtocol(tTransport);

        helloServiceClient = new HelloService.Client(tProtocol);

    }

    public HelloService.Client getHelloServiceClient() {
        return helloServiceClient;
    }

    public void open() throws TTransportException {
        if(tTransport !=null && !tTransport.isOpen() ){
            tTransport.open();
        }
    }

    public void close(){
        if(tTransport !=null && tTransport.isOpen()){
            tTransport.close();
        }
    }


}
