package com.ruoyi.order.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderSummaryDTO
{
    private Long id;

    private Long storeId;

    private Long tableId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private String status;

    private Date createTime;

    private List<OrderItemDTO> items;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount()
    {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount)
    {
        this.payAmount = payAmount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public List<OrderItemDTO> getItems()
    {
        return items;
    }

    public void setItems(List<OrderItemDTO> items)
    {
        this.items = items;
    }
}
