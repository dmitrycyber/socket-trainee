package com.test.socketserver.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class ClientTask implements Runnable {
    @Getter
    @Setter
    private Long userId;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private MessageDispatcher messageDispatcher;

    public ClientTask(Socket socket, MessageDispatcher messageDispatcher) throws IOException {
        this.socket = socket;
        this.messageDispatcher = messageDispatcher;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    @Override
    public void run() {
        readMessage();
    }

    private void readMessage() {
        byte[] buffer;
        String resultMessage = null;
        try {
            int bytesToRead = inputStream.available();

            if (bytesToRead > 0){
                buffer = getBytesFromInputStream();
                resultMessage = new String(buffer, 0, bytesToRead, StandardCharsets.UTF_8);
            }

            while (userId == null) {
                if (resultMessage != null) {
                    log.info("Register user message " + resultMessage);
                    messageDispatcher.register(resultMessage, this);
                }

                resultMessage = null;
            }

            messageDispatcher.processMessage(resultMessage, userId);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytesFromInputStream(){
        byte[] buffer = new byte[4096];

        try {
            int bytesToRead = inputStream.available();

            if (bytesToRead > 0) {
                inputStream.read(buffer, 0, bytesToRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public void writeMessage(String message) throws IOException {
        synchronized (ClientTask.class){
            if (outputStream != null){
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
