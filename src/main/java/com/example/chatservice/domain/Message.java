package com.example.chatservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String fromURL;
    private String toURL;
    private String content;
}
