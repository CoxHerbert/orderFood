package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.order.domain.dto.MerchantOrderListItem;
import com.ruoyi.order.service.IOrderService;

@RestController
@RequestMapping("/api/merchant")
public class MerchantOrderController
{
    private final IOrderService orderService;

    public MerchantOrderController(IOrderService orderService)
    {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public AjaxResult listOrders(@RequestParam Long storeId, @RequestParam(required = false) String status)
    {
        List<MerchantOrderListItem> list = orderService.listMerchantOrders(storeId, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/orders/{id}")
    public AjaxResult getOrderDetail(@PathVariable Long id)
    {
        return AjaxResult.success(orderService.getOrderDetail(id));
    }

    @PostMapping("/orders/{id}/confirm")
    public AjaxResult confirmOrder(@PathVariable Long id)
    {
        orderService.updateOrderStatus(id, "CONFIRMED");
        return AjaxResult.success();
    }

    @PostMapping("/orders/{id}/ready")
    public AjaxResult markReady(@PathVariable Long id)
    {
        orderService.updateOrderStatus(id, "READY");
        return AjaxResult.success();
    }
}
