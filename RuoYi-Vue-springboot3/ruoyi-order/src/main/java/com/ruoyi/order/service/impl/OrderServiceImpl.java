package com.ruoyi.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.order.domain.Order;
import com.ruoyi.order.domain.OrderItem;
import com.ruoyi.order.domain.ProductSku;
import com.ruoyi.order.domain.ProductSpu;
import com.ruoyi.order.domain.dto.CreateOrderRequest;
import com.ruoyi.order.domain.dto.CreateOrderRequest.CreateOrderItem;
import com.ruoyi.order.domain.dto.MerchantOrderListItem;
import com.ruoyi.order.domain.dto.OrderItemDTO;
import com.ruoyi.order.domain.dto.OrderSummaryDTO;
import com.ruoyi.order.mapper.OrderItemMapper;
import com.ruoyi.order.mapper.OrderMapper;
import com.ruoyi.order.mapper.ProductSkuMapper;
import com.ruoyi.order.mapper.ProductSpuMapper;
import com.ruoyi.order.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService
{
    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final ProductSkuMapper productSkuMapper;

    private final ProductSpuMapper productSpuMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, ProductSkuMapper productSkuMapper, ProductSpuMapper productSpuMapper)
    {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productSkuMapper = productSkuMapper;
        this.productSpuMapper = productSpuMapper;
    }

    @Override
    @Transactional
    public OrderSummaryDTO createOrder(CreateOrderRequest request)
    {
        if (request == null || request.getItems() == null || request.getItems().isEmpty())
        {
            throw new ServiceException("订单条目不能为空");
        }

        Map<Long, ProductSpu> spuCache = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderItem item : request.getItems())
        {
            if (item.getQuantity() == null || item.getQuantity() < 1)
            {
                throw new ServiceException("数量必须大于0");
            }
            ProductSku sku = productSkuMapper.selectProductSkuById(item.getSkuId());
            if (sku == null || sku.getAvailable() == null || sku.getAvailable() == 0)
            {
                throw new ServiceException("商品不可售");
            }

            ProductSpu spu = spuCache.computeIfAbsent(sku.getSpuId(), id -> productSpuMapper.selectProductSpuById(id));
            if (spu == null || spu.getOnSale() == null || spu.getOnSale() == 0)
            {
                throw new ServiceException("商品未上架");
            }

            BigDecimal lineTotal = sku.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setSpuId(spu.getId());
            orderItem.setSkuId(sku.getId());
            orderItem.setProductName(spu.getName());
            orderItem.setSkuName(sku.getSkuName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(sku.getPrice());
            orderItem.setTotalPrice(lineTotal);
            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setStoreId(request.getStoreId());
        order.setTableId(request.getTableId());
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setStatus("CREATED");
        orderMapper.insertOrder(order);

        for (OrderItem orderItem : orderItems)
        {
            orderItem.setOrderId(order.getId());
        }
        if (!orderItems.isEmpty())
        {
            orderItemMapper.batchInsertOrderItems(orderItems);
        }

        return getOrderDetail(order.getId());
    }

    @Override
    public OrderSummaryDTO getOrderDetail(Long orderId)
    {
        Order order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            return null;
        }
        List<OrderItem> items = orderItemMapper.selectOrderItemsByOrderId(orderId);

        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setId(order.getId());
        dto.setStoreId(order.getStoreId());
        dto.setTableId(order.getTableId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPayAmount(order.getPayAmount());
        dto.setStatus(order.getStatus());
        dto.setCreateTime(order.getCreateTime());

        List<OrderItemDTO> itemDTOS = new ArrayList<>();
        for (OrderItem item : items)
        {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductName(item.getProductName());
            itemDTO.setSkuName(item.getSkuName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setUnitPrice(item.getUnitPrice());
            itemDTO.setTotalPrice(item.getTotalPrice());
            itemDTOS.add(itemDTO);
        }
        dto.setItems(itemDTOS);
        return dto;
    }

    @Override
    public int updateOrderStatus(Long orderId, String status)
    {
        return orderMapper.updateOrderStatus(orderId, status);
    }

    @Override
    public List<MerchantOrderListItem> listMerchantOrders(Long storeId, String status)
    {
        List<Order> orders = orderMapper.selectOrdersByStoreAndStatus(storeId, status);
        List<MerchantOrderListItem> list = new ArrayList<>();
        for (Order order : orders)
        {
            MerchantOrderListItem item = new MerchantOrderListItem();
            item.setId(order.getId());
            item.setStoreId(order.getStoreId());
            item.setTableId(order.getTableId());
            item.setTotalAmount(order.getTotalAmount());
            item.setPayAmount(order.getPayAmount());
            item.setStatus(order.getStatus());
            item.setCreateTime(order.getCreateTime());
            list.add(item);
        }
        return list;
    }
}
