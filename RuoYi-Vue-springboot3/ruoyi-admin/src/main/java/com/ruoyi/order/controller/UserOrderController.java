package com.ruoyi.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.order.domain.dto.CreateOrderRequest;
import com.ruoyi.order.domain.dto.OrderSummaryDTO;
import com.ruoyi.order.service.IOrderService;

@RestController
@RequestMapping("/api/user")
public class UserOrderController extends BaseController
{
    private final IOrderService orderService;

    public UserOrderController(IOrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public AjaxResult createOrder(@RequestBody CreateOrderRequest request)
    {
        OrderSummaryDTO dto = orderService.createOrder(request);
        return AjaxResult.success(dto);
    }

    @GetMapping("/orders/{id}")
    public AjaxResult getOrder(@PathVariable Long id)
    {
        return AjaxResult.success(orderService.getOrderDetail(id));
    }
}
