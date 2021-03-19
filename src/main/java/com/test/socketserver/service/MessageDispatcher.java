package com.test.socketserver.service;

public interface MessageDispatcher {
    void register(String message, ClientTask clientTask);

    void processMessage(String message);
}
