package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.dto.CreateOrderRequest;
import com.ruoyi.order.domain.dto.MerchantOrderListItem;
import com.ruoyi.order.domain.dto.OrderSummaryDTO;

/**
 * 订单服务接口
 */
public interface IOrderService
{
    OrderSummaryDTO createOrder(CreateOrderRequest request);

    OrderSummaryDTO getOrderDetail(Long orderId);

    int updateOrderStatus(Long orderId, String status);

    List<MerchantOrderListItem> listMerchantOrders(Long storeId, String status);
}
