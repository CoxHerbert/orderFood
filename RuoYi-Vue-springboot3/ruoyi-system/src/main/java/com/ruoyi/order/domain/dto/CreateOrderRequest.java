package com.ruoyi.order.domain.dto;

import java.util.List;

public class CreateOrderRequest
{
    private Long storeId;

    private Long tableId;

    private String userId;

    private List<CreateOrderItem> items;

    public Long getStoreId()
    {
        return storeId;
    }

    public void setStoreId(Long storeId)
    {
        this.storeId = storeId;
    }

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public List<CreateOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<CreateOrderItem> items)
    {
        this.items = items;
    }

    public static class CreateOrderItem
    {
        private Long skuId;

        private Integer quantity;

        public Long getSkuId()
        {
            return skuId;
        }

        public void setSkuId(Long skuId)
        {
            this.skuId = skuId;
        }

        public Integer getQuantity()
        {
            return quantity;
        }

        public void setQuantity(Integer quantity)
        {
            this.quantity = quantity;
        }
    }
}
