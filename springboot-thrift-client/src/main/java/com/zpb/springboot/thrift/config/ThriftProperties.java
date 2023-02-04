package com.zpb.springboot.thrift.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author       pengbo.zhao
 * @description  thrift-properties
 * @createDate   2022/1/5 14:20
 * @updateDate   2022/1/5 14:20
 * @version      1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "thrift")
public class ThriftProperties {

    private String host;

    private int port;

}
