package org.hroubles.mir.controller;

import org.hroubles.mir.domain.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message sendMessage(Message message) {
        return message;
    }
}
