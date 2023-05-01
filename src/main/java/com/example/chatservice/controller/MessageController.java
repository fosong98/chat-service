package com.example.chatservice.controller;

import com.example.chatservice.domain.Message;
import com.example.chatservice.repository.MessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final RestTemplateBuilder restTemplateBuilder;
    private final MessageRepository messageRepository;
    @Value("${RMSG}")
    String returnMessage;

    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("messages", messageRepository.getAll());

        return "main";
    }

    @PostMapping("/")
    public String sendMessage(HttpServletRequest request,
                              @RequestParam String toUrl,
                              @RequestParam String content) {
        messageRepository.save(new Message(
                request.getRequestURL().toString(),
                toUrl, content
        ));

        // 메시지 전송 및 캐치
        try {
            String responseMessage = restTemplateBuilder.build().getForObject(toUrl, String.class);
            messageRepository.save(new Message(
                    toUrl,
                    request.getRequestURL().toString(),
                    responseMessage));
        } catch (Exception e) {
            messageRepository.save(new Message(
                    toUrl,
                    request.getRequestURL().toString(),
                    "failed"));
            e.printStackTrace();
        }

        return "redirect:";
    }

    @ResponseBody
    @GetMapping("/message")
    public String receiveMessage() {
        return returnMessage;
    }
}
