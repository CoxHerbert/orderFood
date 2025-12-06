package com.ruoyi.order.service;

import java.util.List;
import com.ruoyi.order.domain.dto.CategoryMenuDTO;

/**
 * 用户端菜单服务
 */
public interface IUserMenuService
{
    List<CategoryMenuDTO> getStoreMenu(Long storeId);
}
