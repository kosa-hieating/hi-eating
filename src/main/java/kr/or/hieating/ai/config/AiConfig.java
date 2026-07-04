package kr.or.hieating.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * AI 관련 설정을 담당하는 구성 클래스.
 *
 * <p>Spring Boot의 조건부 빈 등록 기능({@link ConditionalOnProperty})을 활용하여,
 * {@code greenfood.ai.enabled} 프로퍼티가 {@code true}이거나 정의되지 않은 경우에만
 * AI 관련 빈({@link ChatClient})을 생성
 */
@Configuration
@ConditionalOnProperty(
    prefix = "greenfood.ai",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class AiConfig {

    /**
     * 원격 Ollama 서버와 통신하기 위한 ChatClient 빈을 생성
     *
     * @param builder Spring AI가 자동 구성한 {@link ChatClient.Builder}
     * @return 프롬프트 호출에 사용할 {@link ChatClient} 인스턴스
     */

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder) {
    return builder.build();
  }
}
