package fun.lance.admin.service;

import fun.lance.admin.domain.dto.OrderDto;

public interface OrderService {

    /**
     * 假设创建了一个订单
     */
    void createOrder(OrderDto order);
}
