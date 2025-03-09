package fun.lance.admin.mq;

import cn.hutool.core.lang.UUID;
import fun.lance.admin.domain.dto.OrderDto;
import fun.lance.admin.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        for (int i = 0; i < 5; i++) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(UUID.randomUUID().toString());
            orderDto.setName("订单" + i);
            orderDto.setCreateTime(new Date());
            orderService.createOrder(orderDto);
            try {
                Thread.sleep(2000);
            } catch (Exception ignored) {}
        }
    }
}
