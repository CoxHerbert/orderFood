package com.ruoyi.order.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.order.domain.Order;

/**
 * 订单Mapper接口
 */
public interface OrderMapper
{
    Order selectOrderById(Long id);

    int insertOrder(Order order);

    int updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);

    List<Order> selectOrdersByStoreAndStatus(@Param("storeId") Long storeId, @Param("status") String status);
}
