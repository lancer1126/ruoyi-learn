package fun.lance.task.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import fun.lance.common.mq.constant.RabbitName;
import fun.lance.task.domain.SubjectAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class SubjectListener {

    /**
     * 接收并处理受试者消息
     */
    @RabbitListener(queues = RabbitName.QUEUE_SUBJECT, concurrency = "1")
    public void handleSubject(@Payload Message message,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                              Channel channel) {
        log.info("接收到受试者消息: {}", message);

        // 转换消息
        String bodyStr = new String(message.getBody(), StandardCharsets.UTF_8);
        SubjectAgent subject = convert(bodyStr);
        try {
            if (subject == null) {
                log.error("转换后的受试者对象为空，拒绝消息,tag-{}", deliveryTag);
                channel.basicReject(deliveryTag, false);
                return;
            }

            processSubject(subject);
            channel.basicAck(deliveryTag, false);
            log.info("受试者消息处理成功，已确认");

        } catch (IOException e) {
            log.error("RabbitMQ IO异常", e);
            // 不要再尝试确认或拒绝，让Spring AMQP处理
        } catch (Exception e) {
            log.error("处理受试者消息时发生错误", e);
            try {
                // 仅当通道仍然开放时才尝试拒绝
                if (channel.isOpen()) {
                    boolean requeue = shouldRequeue(e);
                    channel.basicReject(deliveryTag, requeue);
                    log.info("受试者消息处理失败，已拒绝，requeue={}", requeue);
                }
            } catch (IOException ioException) {
                log.error("拒绝消息时发生IO异常", ioException);
            }
        }
    }

    /**
     * 转换消息为 SubjectAgent 对象
     * @param bodyStr 消息内容
     * @return 转换后的 SubjectAgent 对象
     */
    private SubjectAgent convert(String bodyStr) {
        SubjectAgent subject = null;
        try {
            subject = JSON.parseObject(bodyStr, SubjectAgent.class);
            if (subject.getSubjectId() == null) {
                log.warn("受试者消息缺少 subjectId");
            }
        } catch (Exception e) {
            log.error("转换受试者消息时发生错误", e);
        }
        return subject;
    }

    /**
     * 处理受试者消息
     */
    private void processSubject(SubjectAgent subject) {
        log.info("受试者编号: {}", subject.getSubjectNo());
        log.info("受试者姓名: {}", subject.getSubjectName());
    }

    /**
     * 决定是否重新入队
     * 可以根据异常类型或其他条件决定是否重新入队
     */
    private boolean shouldRequeue(Exception e) {
        // 根据异常类型判断是否需要重新入队
        // 例如网络异常、临时故障等可以重试，而数据格式错误等则不需要
        if (e instanceof IOException || e instanceof TimeoutException) {
            return true;
        }
        // 业务逻辑错误、格式错误等通常不需要重试
        if (e instanceof IllegalArgumentException) {
            return false;
        }

        // 默认重试
        return true;
    }
}
