package kr.or.hieating.global.apiPayload.exception.handler;

import kr.or.hieating.global.apiPayload.code.BaseErrorCode;
import kr.or.hieating.global.apiPayload.exception.GeneralException;

public class PromotionHandler extends GeneralException {
  public PromotionHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
