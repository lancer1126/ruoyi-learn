package fun.lance.admin.config.mq;

import fun.lance.common.mq.constant.RabbitName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitSubjectConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitName.EXCHANGE_SUBJECT);
    }

    @Bean
    public Queue subjectQueue() {
        // durable为true表示队列持久化
        return new Queue(RabbitName.QUEUE_SUBJECT, true);
    }

    @Bean
    public Binding bindingSubject() {
        return BindingBuilder.bind(subjectQueue())
                .to(directExchange())
                .with(RabbitName.ROUTING_SUBJECT);
    }

    @Bean
    public MessageReturnListener messageReturnListener() {
        return new MessageReturnListener();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageReturnListener returnListener) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 使用Jackson2JsonMessageConverter来支持复杂对象的序列化
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        // 设置消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息成功发送到交换机: {}",
                        correlationData != null ? correlationData.getId() : "未知");
            } else {
                log.error("消息发送到交换机失败: {}, 原因: {}",
                        correlationData != null ? correlationData.getId() : "未知",
                        cause);
            }
        });

        // 消息路由失败回调（当消息无法路由到队列时触发）
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("消息路由失败: exchange={}, routingKey={}, message={}, replyCode={}, replyText={}",
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    returned.getMessage(),
                    returned.getReplyCode(),
                    returned.getReplyText());

            returnListener.handleRoutingFailure(returned);
        });

        // 设置mandatory为true，消息路由失败时才会回调ReturnsCallback
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }
}
