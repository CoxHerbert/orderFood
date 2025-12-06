package com.ruoyi.order.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 桌台实体
 */
public class StoreTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Excel(name = "ID", cellType = ColumnType.NUMERIC)
    private Long id;

    /** 门店ID */
    @Excel(name = "门店ID", cellType = ColumnType.NUMERIC)
    private Long storeId;

    /** 桌码 */
    @Excel(name = "桌码")
    private String tableCode;

    /** 桌名 */
    @Excel(name = "桌名")
    private String tableName;

    /** 二维码内容 */
    @Excel(name = "二维码内容")
    private String qrcodeContent;

    /** 是否启用 */
    @Excel(name = "是否启用", readConverterExp = "0=停用,1=启用")
    private Integer enabled;

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

    public String getTableCode()
    {
        return tableCode;
    }

    public void setTableCode(String tableCode)
    {
        this.tableCode = tableCode;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getQrcodeContent()
    {
        return qrcodeContent;
    }

    public void setQrcodeContent(String qrcodeContent)
    {
        this.qrcodeContent = qrcodeContent;
    }

    public Integer getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Integer enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeId", getStoreId())
                .append("tableCode", getTableCode())
                .append("tableName", getTableName())
                .append("qrcodeContent", getQrcodeContent())
                .append("enabled", getEnabled())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
