package kr.or.hieating.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 원격 Ollama LLM과의 채팅 프롬프트 호출을 담당하는 서비스.
 *
 * <p>{@link ChatClient}를 통해 사용자 프롬프트를 전달하고, 모델의 응답 텍스트 반환
 * {@code greenfood.ai.enabled} 프로퍼티가 {@code true}이거나 정의되지 않은 경우에만 빈으로 등록
 */

@Service
@ConditionalOnProperty(
    prefix = "greenfood.ai",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class AiChatService {

  private final ChatClient chatClient;

    /**
     * @param chatClient 원격 Ollama 서버 호출에 사용할 {@link ChatClient}
    */
  public AiChatService(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

    /**
     * 주어진 프롬프트를 원격 Ollama 모델에 전달하고 응답 텍스트 반환
     *
     * @param prompt 모델에 전달할 사용자 프롬프트 (공백 불가)
     * @return 모델이 생성한 응답 텍스트
     * @throws IllegalArgumentException prompt가 null이거나 공백일 경우
     */
  public String chat(String prompt) {
    Assert.hasText(prompt, "prompt must not be blank");
    return chatClient.prompt().user(prompt).call().content();
  }
}
