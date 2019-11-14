package org.net;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppServerTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("服务启动成功");
            while (!serverSocket.isClosed()) {
                final Socket requset = serverSocket.accept();
                System.out.println("收到新连接" + requset.toString());
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100000000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            InputStream inputStream = requset.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                            String msg;
                            while ((msg = bufferedReader.readLine()) != null) {
                                if (msg.length() == 0) {
                                    break;
                                }
                                System.out.println(msg);
                            }
                            System.out.println("收到数据，来自" + requset.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
