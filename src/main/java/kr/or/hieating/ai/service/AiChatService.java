package kr.or.hieating.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@ConditionalOnProperty(
    prefix = "greenfood.ai",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class AiChatService {

  private final ChatClient chatClient;

  public AiChatService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public String chat(String prompt) {
    Assert.hasText(prompt, "prompt must not be blank");
    return chatClient.prompt().user(prompt).call().content();
  }
}
