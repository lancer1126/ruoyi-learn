package fun.lance.admin.listener;

import com.rabbitmq.client.Channel;
import fun.lance.common.mq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadLetterConsumer {

    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUE, ackMode = "MANUAL")
    public void handleDeadLetterMessage(String orderJson, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("死信队列接收到订单: {}", orderJson);
        // 更新订单相关信息
        updateOrder(orderJson);
        // 手动确认信息
        try {
            channel.basicAck(tag, true);
        } catch (Exception e) {
            log.error("删除消息失败: order-{}, message: {}",orderJson, e.getMessage());
            retryAck();
        }
    }

    private void updateOrder(String orderJson) {
    }

    private void retryAck() {
    }
}
