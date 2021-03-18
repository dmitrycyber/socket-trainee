package com.test.socketserver.controller.impl;

import com.test.socketserver.controller.SocketController;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.ServerSocket;
import java.net.Socket;

@Controller
@Slf4j
public class SocketControllerImpl implements SocketController {

    @Value("${server.socket.port}")
    private int serverSocketPort;

    @Override
    @SneakyThrows
    public void handleClientSocket() {
        ServerSocket serverSocket = new ServerSocket(serverSocketPort);
        log.info("Server is running...");
        log.info("Listening port: " + serverSocketPort);

        while(true){
            Socket clientSocket = serverSocket.accept();
            log.info("Client connected");

        }

    }
}
