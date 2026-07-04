package kr.or.hieating.ai.controller;

import kr.or.hieating.ai.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiTestController {

    private final AiChatService aiChatService;

    @GetMapping("/test")
    public String test() {
        return aiChatService.chat("안녕하세요. 자기소개 한 줄 해줘.");
    }
}
