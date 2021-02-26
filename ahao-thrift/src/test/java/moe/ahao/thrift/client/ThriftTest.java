package moe.ahao.thrift.client;

import moe.ahao.thrift.gen.StudentService;
import moe.ahao.thrift.service.StudentServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
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
        TServerSocket serverTransport = new TServerSocket(port);
        TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
        TProcessor processor = new StudentService.Processor<>(new StudentServiceImpl());

        TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
        serverArgs.processor(processor);
        serverArgs.protocolFactory(proFactory);

        server = new TThreadPoolServer(serverArgs);
        new Thread(() -> server.serve()).start();
    }

    @AfterAll
    public static void afterAll() {
        server.stop();
    }

    @Test
    public void test() throws Exception {
        try(TTransport transport = new TSocket("127.0.0.1", port);) {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);

            StudentService.Client client = new StudentService.Client(protocol);

            String name = "ahao";
            String result = client.hello(name);
            System.out.println(result);
            Assertions.assertEquals(new StudentServiceImpl().hello(name), result);
        }
    }
}
