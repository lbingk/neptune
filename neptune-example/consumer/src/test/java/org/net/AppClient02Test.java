package org.net;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppClient02Test {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 100; i++) {
                Socket socket = new Socket("localhost", 8080);
                OutputStream outputStream = socket.getOutputStream();
                String smg = String.valueOf(i);
                outputStream.write(smg.getBytes(StandardCharsets.UTF_8));
                socket.close();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
