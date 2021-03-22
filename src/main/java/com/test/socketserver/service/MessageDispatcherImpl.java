package com.test.socketserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.socketserver.model.SocketMessageType;
import com.test.socketserver.model.UserDto;
import com.test.socketserver.model.Wrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageDispatcherImpl implements MessageDispatcher {
    private static final Map<Long, ClientTask> tasks = Collections.synchronizedMap(new HashMap<>());
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void register(String message, ClientTask clientTask) {
        Wrapper<?> wrapper = defineMessage(message);

        if (wrapper != null){
            log.info("Current client tasks: " + tasks);

            log.info("Message from socket " + wrapper.getMessage());

            UserDto userDto = (UserDto) wrapper.getMessage();
            Long userId = userDto.getUserId();

            clientTask.setUserId(userId);
            tasks.put(userId, clientTask);

            log.info(String.format("User %d registered", userId));
            log.info("Current client tasks: " + tasks);
        }
    }

    @Override
    public void processMessage(String message, Long userId) {
        //TODO add task to queue before execute
    }

    @SneakyThrows
    private Wrapper<?> defineMessage(String message) {
        Wrapper<Object> wrapper = objectMapper.readValue(message, new TypeReference<>() {
        });

        if (wrapper.getMessageType().equals(SocketMessageType.REGISTER)) {
            return objectMapper.readValue(message, new TypeReference<Wrapper<UserDto>>() {
            });
        }


        return null;
    }
}
