package kr.or.hieating.ai.dto;

import java.util.List;

public record EmailQualityValidationResult(
    boolean spellingValid,
    boolean contextValid,
    boolean productInfoValid,
    boolean discountRateValid,
    boolean exaggerationFree,
    boolean lengthValid,
    List<String> issues) {

  public EmailQualityValidationResult {
    issues = issues == null ? List.of() : List.copyOf(issues);
  }

  public boolean isPass() {
    return spellingValid
        && contextValid
        && productInfoValid
        && discountRateValid
        && exaggerationFree
        && lengthValid;
  }

  public String reason() {
    if (isPass()) {
      return "모든 이메일 품질 검증 항목을 통과했습니다.";
    }
    if (issues.isEmpty()) {
      return "AI 검증에서 품질 기준을 통과하지 못했으나 상세 사유가 제공되지 않았습니다.";
    }
    return String.join("; ", issues);
  }
}
