package com.zpb.springboot.thrift.config;

import api.HelloService;
import com.zpb.springboot.thrift.service.impl.HelloServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author       pengbo.zhao
 * @description  thrift-config
 * @createDate   2022/1/4 16:49
 * @updateDate   2022/1/4 16:49
 * @version      1.0
 */
@Component
public class ThriftServer implements DisposableBean {

    private TServer server;

    @Resource
    private ThriftProperties thriftProperties;

    @Resource
    private HelloServiceImpl helloServiceImpl;

    @PostConstruct
    public void start(){

        // thrift支持非阻塞的 socket
        TNonblockingServerSocket socket = null;
        try {
            socket = new TNonblockingServerSocket(thriftProperties.getThriftPort());

            // thrift-服务端实现类
            HelloService.Processor<HelloServiceImpl> helloServiceProcessor = new HelloService.Processor<HelloServiceImpl>(helloServiceImpl);

            THsHaServer.Args arg = new THsHaServer.Args(socket)
                    .minWorkerThreads(thriftProperties.getThriftMinThreadsSize())
                    .maxWorkerThreads(thriftProperties.getThriftMaxThreadsSize())
                    ;

            // 线程池
            arg.executorService(createExecutorService());

            //---------------thrift传输协议------------------------------
            //1. TBinaryProtocol      二进制传输协议
            //2. TCompactProtocol     压缩协议 他是基于TBinaryProtocol二进制协议在进一步的压缩，使得体积更小
            //3. TJSONProtocol        Json格式传输协议
            //4. TSimpleJSONProtocol  简单JSON只写协议，生成的文件很容易通过脚本语言解析，实际开发中很少使用
            //5. TDebugProtocol       简单易懂的可读协议，调试的时候用于方便追踪传输过程中的数据
            //-----------------------------------------------------------
            // 设置传输协议工厂
            TProtocolFactory thriftProtocolFactory = new TBinaryProtocol.Factory();
            arg.protocolFactory(thriftProtocolFactory);

            //---------------thrift数据传输方式------------------------------
            //1. TSocket            阻塞式Socket 相当于Java中的ServerSocket
            //2. TFrameTransport    以frame为单位进行数据传输，非阻塞式服务中使用
            //3. TFileTransport     以文件的形式进行传输
            //4. TMemoryTransport   将内存用于IO,Java实现的时候内部实际上是使用了简单的ByteArrayOutputStream
            //5. TZlibTransport     使用zlib进行压缩，与其他传世方式联合使用；java当前无实现所以无法使用
            //传输工厂 更加底层的概念
            arg.transportFactory(new TFramedTransport.Factory());


            //设置处理器(Processor)工厂
            arg.processorFactory(new TProcessorFactory(helloServiceProcessor));

            //--------------- thrift支持的服务模型 ------------------------------
            //1.TSimpleServer  简单的单线程服务模型，用于测试
            //2.TThreadPoolServer 多线程服务模型，使用的标准的阻塞式IO;运用了线程池，当线程池不够时会创建新的线程,当线程池出现大量空闲线程，线程池会对线程进行回收
            //3.TNonBlockingServer 多线程服务模型，使用非阻塞式IO（需要使用TFramedTransport数据传输方式）
            //4.THsHaServer YHsHa引入了线程池去处理（需要使用TFramedTransport数据传输方式），其模型把读写任务放到线程池去处理;Half-sync/Half-async（半同步半异步）的处理模式;Half-sync是在处理IO时间上（sccept/read/writr io）,Half-async用于handler对RPC的同步处理
            //----------------------------
            //根据参数实例化server
            //半同步半异步的server
            server = new THsHaServer(arg);
            System.err.println("thrift server start ......" + thriftProperties.getThriftPort());
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
            System.err.println("thrift server 启动失败 ......");
        }


    }


    /**
     * 创建线程池
     * @return thread pool
     */
    private ExecutorService createExecutorService(){
        return new ThreadPoolExecutor(thriftProperties.getThriftCoreThreads(),
                thriftProperties.getThriftMaxThreadsSize(),60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public void destroy() throws Exception {
        System.err.println("thrift server stop ......" + thriftProperties.getThriftPort());
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        System.err.println("shut down thrift server ");
        server.stop();
    }

}
