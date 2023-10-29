package com.example.kafkaexample;

import com.google.gson.Gson;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private KafkaTemplate<String, String> kafkaTemplate;


    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void publish(@RequestBody Test test) {
        Gson gson = new Gson();
        String message = gson.toJson(test);
        kafkaTemplate.send("amigoscode1", message);
    }

    private Test lastMessage; // Lưu trữ tin nhắn gần nhất

    @KafkaListener(topics = "amigoscode1", groupId = "my-group-id")
    public Test consumeMessage(String message) {
        // Lưu tin nhắn gần nhất
        Gson gson = new Gson();
        Test test = gson.fromJson(message, Test.class);
        lastMessage = test;
       return lastMessage;
    }

    @GetMapping()
    public Test getLastMessage() {
        return lastMessage;
    }
}
