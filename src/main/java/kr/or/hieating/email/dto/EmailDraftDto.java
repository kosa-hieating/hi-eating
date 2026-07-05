package kr.or.hieating.email.dto;

import java.time.LocalDateTime;
import kr.or.hieating.email.domain.EmailPublishStatus;
import kr.or.hieating.email.domain.EmailValidationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDraftDto {

  private Long id;
  private Long hotDealId;
  private Long userId;
  private String recipientName;
  private String recipientEmail;
  private String hotDealTitle;
  private String subject;
  private String content;
  private EmailValidationStatus validationStatus;
  private String validationReason;
  private EmailPublishStatus publishStatus;
  private String publishErrorMessage;
  private LocalDateTime publishedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public String getValidationStatusLabel() {
    if (validationStatus == null) {
      return "검증 대기";
    }

    return switch (validationStatus) {
      case PASS -> "검증 통과";
      case FAIL -> "검증 필요";
      case PENDING -> "검증 대기";
    };
  }

  public String getValidationStatusClass() {
    if (validationStatus == null) {
      return "is-warning";
    }

    return switch (validationStatus) {
      case PASS -> "is-success";
      case FAIL -> "is-danger";
      case PENDING -> "is-warning";
    };
  }

  public String getPublishStatusLabel() {
    if (publishStatus == null) {
      return "발행 대기";
    }

    return switch (publishStatus) {
      case READY -> "발행 대기";
      case PUBLISHED -> "발행 완료";
      case FAILED -> "발행 실패";
    };
  }

  public String getPublishStatusClass() {
    if (publishStatus == null) {
      return "is-brand";
    }

    return switch (publishStatus) {
      case READY -> "is-brand";
      case PUBLISHED -> "is-success";
      case FAILED -> "is-danger";
    };
  }

  public boolean isValidationFailed() {
    return validationStatus == EmailValidationStatus.FAIL;
  }

  public boolean isPublishable() {
    return publishStatus != EmailPublishStatus.PUBLISHED;
  }
}
