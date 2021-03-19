package com.test.socketserver.controller.impl;

import com.test.socketserver.controller.SocketController;
import com.test.socketserver.service.ClientTask;
import com.test.socketserver.service.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SocketControllerImpl implements SocketController {
    private final MessageDispatcher messageDispatcher;

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

            ClientTask clientTask = new ClientTask(clientSocket, messageDispatcher);

            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(clientTask);

            log.info("Working on...");
        }

    }
}
