package kr.or.hieating.global.apiPayload.code;

public interface BaseErrorCode {
  ErrorReasonDTO getReason();

  ErrorReasonDTO getReasonHttpStatus();
}
