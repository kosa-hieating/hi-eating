package kr.or.hieating.ai.service;

import java.util.List;
import kr.or.hieating.ai.mapper.HotDealEmailContentMapper;
import kr.or.hieating.email.publisher.EmailPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
    prefix = "greenfood.ai",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class HotDealEmailAutoPublishService {

  private final HotDealEmailContentMapper contentMapper;
  private final EmailPublisher emailPublisher;

  public int publishApprovedEmails(long hotDealId) {
    List<Long> emailSendLogIds = contentMapper.findApprovedEmailSendLogIds(hotDealId);

    for (Long emailSendLogId : emailSendLogIds) {
      emailPublisher.publish(emailSendLogId);
    }

    log.info("[이메일 자동 발행] 완료 hotDealId={} 발행={}건", hotDealId, emailSendLogIds.size());
    return emailSendLogIds.size();
  }
}
