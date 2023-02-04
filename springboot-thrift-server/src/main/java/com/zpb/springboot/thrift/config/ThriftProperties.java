package com.zpb.springboot.thrift.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author       pengbo.zhao
 * @description  thrift-properties
 * @createDate   2022/1/4 16:33
 * @updateDate   2022/1/4 16:33
 * @version      1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "thrift")
public class ThriftProperties {

    /**
     * 端口
     */
    private int thriftPort;

    /**
     * 超时时间
     */
    private int thriftTimeout;

    /**
     * 阻塞时间
     */
    private int thriftTcpBacklog;

    /**
     * 核心线程数
     */
    private int thriftCoreThreads;

    /**
     * 最小线程数
     */
    private int thriftMinThreadsSize;

    /**
     * 最大线程数
     */
    private int thriftMaxThreadsSize;

    /**
     * 选择线程数
     */
    private int thriftSelectorThreads;

    /**
     * frame-size
     */
    private int thriftMaxFrameSize;

    /**
     * read-buffer-size
     */
    private int thriftMaxReadBufSize;

    public int getThriftMaxFrameSize() {

        return thriftMaxFrameSize * mb();
    }

    public int getThriftMaxReadBufSize() {
        return thriftMaxReadBufSize * mb();
    }


    /**
     * 1 MB
     * @return int
     */
    private int mb(){
        return 1024 * 1024;
    }

}
