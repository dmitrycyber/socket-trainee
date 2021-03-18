package com.test.socketserver.configuration;

import com.test.socketserver.controller.SocketController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
    private final SocketController socketController;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        socketController.handleClientSocket();
    }
}
