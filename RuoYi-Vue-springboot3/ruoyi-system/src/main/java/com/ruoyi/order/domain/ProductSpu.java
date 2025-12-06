package com.ruoyi.order.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 菜品SPU实体
 */
public class ProductSpu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 门店ID */
    @Excel(name = "门店ID", cellType = ColumnType.NUMERIC)
    private Long storeId;

    /** 分类ID */
    @Excel(name = "分类ID", cellType = ColumnType.NUMERIC)
    private Long categoryId;

    /** 菜品名称 */
    @Excel(name = "菜品名称")
    private String name;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 主图 */
    @Excel(name = "主图")
    private String imageUrl;

    /** 是否上架 */
    @Excel(name = "是否上架", readConverterExp = "0=下架,1=上架")
    private Integer onSale;

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

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Integer getOnSale()
    {
        return onSale;
    }

    public void setOnSale(Integer onSale)
    {
        this.onSale = onSale;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("categoryId", getCategoryId())
                .append("name", getName())
                .append("description", getDescription())
                .append("imageUrl", getImageUrl())
                .append("onSale", getOnSale())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
