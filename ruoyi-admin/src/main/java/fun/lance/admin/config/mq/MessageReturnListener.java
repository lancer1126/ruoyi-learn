package fun.lance.admin.config.mq;

import fun.lance.admin.domain.entity.Subject;
import fun.lance.admin.repository.SubjectMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReturnListener {

    @Resource
    private SubjectMapper subjectMapper;
    @Resource
    private Jackson2JsonMessageConverter jsonMessageConverter;

    public void handleRoutingFailure(ReturnedMessage returned) {
        try {
            // 从消息中提取Subject对象
            Object messageBody = jsonMessageConverter.fromMessage(returned.getMessage());
            if (messageBody instanceof Subject subject) {
                // 更新状态为路由失败
                subject.setSyncStatus(-2);
                subjectMapper.updateById(subject);
                log.info("已更新受试者 {} 的同步状态为路由失败", subject.getSubjectId());
            }
        } catch (Exception e) {
            log.error("处理路由失败消息时发生错误", e);
        }
    }
}
