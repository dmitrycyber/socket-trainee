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
    @Getter @Setter
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
        writeMessage();
    }

    private void readMessage() {
        byte[] buffer = new byte[1024];
        String resultMessage = null;
        int read;
        try {
            while (userId == null) {
                while ((read = inputStream.read(buffer)) != -1) {
                    resultMessage = new String(buffer, 0, read);
                }

                log.info("Register message " + resultMessage);

                messageDispatcher.register(resultMessage, this);
            }

            while ((read = inputStream.read(buffer)) != -1) {
                resultMessage = new String(buffer, 0, read);
            }

            //TODO send dto with userId to message dispatcher


//            if (inputStream.read() > 0){
//                Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//                for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0;) {
//                    sb.append(buffer, 0, numRead);
//                }
//                resultMessage = sb.toString();
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("RESULT MESSAGE " + resultMessage);
        //TODO send to message dispatcher
    }

    private void writeMessage() {

    }
}
