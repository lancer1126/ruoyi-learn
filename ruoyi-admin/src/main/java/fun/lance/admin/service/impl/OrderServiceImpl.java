package fun.lance.admin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.lance.admin.domain.dto.OrderDto;
import fun.lance.admin.service.OrderService;
import fun.lance.common.mq.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void createOrder(OrderDto order) {
        String orderJson;
        try {
            orderJson = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            log.error("order转换异常：{}", order);
            throw new RuntimeException(e);
        }
        log.info("发送订单到队列：{}", orderJson);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE, RabbitConfig.ORDER_ROUTING_KEY, orderJson, msg -> {
            msg.getMessageProperties().setExpiration("30000");
            return msg;
        });
    }
}
