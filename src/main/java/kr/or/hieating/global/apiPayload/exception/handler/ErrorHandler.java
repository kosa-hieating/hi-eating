package kr.or.hieating.global.apiPayload.exception.handler;

import kr.or.hieating.global.apiPayload.code.BaseErrorCode;
import kr.or.hieating.global.apiPayload.exception.GeneralException;

public class ErrorHandler extends GeneralException {
  public ErrorHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
