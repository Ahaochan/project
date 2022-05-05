package moe.ahao.thrift.client;

import moe.ahao.thrift.gen.StudentService;
import moe.ahao.thrift.service.StudentServiceImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ThriftTest {
    public static final int port = 9090;

    private static TServer server;

    @BeforeAll
    public static void beforeAll() throws Exception {
        // 打开一个Socket连接, 监听9090端口
        TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(new TServerSocket(port))
            // 设置监听处理器
            .processor(new StudentService.Processor<>(new StudentServiceImpl()))
            // 设置协议类型
            .protocolFactory(new TBinaryProtocol.Factory());

        server = new TThreadPoolServer(serverArgs);
        new Thread(() -> server.serve()).start();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        // TODO org.apache.thrift.transport.TTransportException: Socket is closed by peer.
        server.setShouldStop(true);
        server.stop();
    }

    @Test
    public void test() throws Exception {
        // 打开一个Socket连接, 监听9090端口
        try(TTransport transport = new TSocket("127.0.0.1", port);) {
            transport.open();

            StudentService.Client client = new StudentService.Client(new TBinaryProtocol(transport));

            String name = "ahao";
            String result = client.hello(name);
            System.out.println(result);
            Assertions.assertEquals(new StudentServiceImpl().hello(name), result);
        }
    }
}
