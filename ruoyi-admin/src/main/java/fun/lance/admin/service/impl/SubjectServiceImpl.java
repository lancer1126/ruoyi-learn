package fun.lance.admin.service.impl;

import fun.lance.admin.domain.entity.Subject;
import fun.lance.admin.repository.SubjectMapper;
import fun.lance.admin.service.SubjectService;
import fun.lance.common.mq.constant.RabbitName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectMapper subjectMapper;
    private final RabbitTemplate rabbitTemplate;

    public void addSubject(Subject subject) {
        try {
            subject.setSyncStatus(1);
            subjectMapper.insert(subject);
            sendToMQ(subject);
        } catch (Exception e) {
            if (subject.getSubjectId() != null) {
                subject.setSyncStatus(-1);
                subjectMapper.updateById(subject);
            }
            log.error("添加受试者信息失败", e);
        }
    }

    private void sendToMQ(Subject subject) {
        String messageId = "subject-" + subject.getSubjectId();
        CorrelationData corrData = new CorrelationData(messageId);

        corrData.getFuture().whenComplete((confirm, exception) -> {
            if (exception != null) {
                // 发生异常
                log.error("受试者 {} 消息确认异常", subject.getSubjectId(), exception);
                handleConfirmFailure(subject, "确认过程发生异常: " + exception.getMessage());
            } else if (confirm != null) {
                // 确认结果返回
                if (confirm.isAck()) {
                    log.info("受试者 {} 发送到交换机成功", subject.getSubjectId());
                    // 可以另开事务更新状态为成功
                    subject.setSyncStatus(2);
                    subjectMapper.updateById(subject);
                } else {
                    log.error("受试者 {} 发送到交换机失败, 原因: {}", subject.getSubjectId(), confirm.getReason());
                    handleConfirmFailure(subject, confirm.getReason());
                }
            }
        });

        rabbitTemplate.convertAndSend(
                RabbitName.EXCHANGE_SUBJECT,
                RabbitName.ROUTING_SUBJECT,
                subject,
                corrData
        );
        log.info("受试者 {} 开始发送到队列", subject.getSubjectId());
    }

    /**
     * 处理确认失败的情况
     */
    private void handleConfirmFailure(Subject subject, String reason) {
        subject.setSyncStatus(-1);
        subjectMapper.updateById(subject);
        log.warn("更新受试者 {} 的同步状态为失败，原因: {}", subject.getSubjectId(), reason);
    }
}
