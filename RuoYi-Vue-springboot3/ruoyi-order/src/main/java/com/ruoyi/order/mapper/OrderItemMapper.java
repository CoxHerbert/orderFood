package com.ruoyi.order.mapper;

import java.util.List;
import com.ruoyi.order.domain.OrderItem;

/**
 * 订单明细Mapper接口
 */
public interface OrderItemMapper
{
    List<OrderItem> selectOrderItemsByOrderId(Long orderId);

    int batchInsertOrderItems(List<OrderItem> items);
}
