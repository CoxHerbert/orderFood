package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.order.domain.dto.CategoryMenuDTO;
import com.ruoyi.order.service.IUserMenuService;

@RestController
@RequestMapping("/api/user")
public class UserMenuController
{
    private final IUserMenuService userMenuService;

    public UserMenuController(IUserMenuService userMenuService)
    {
        this.userMenuService = userMenuService;
    }

    @GetMapping("/stores/{storeId}/menu")
    public AjaxResult getStoreMenu(@PathVariable Long storeId)
    {
        List<CategoryMenuDTO> menu = userMenuService.getStoreMenu(storeId);
        return AjaxResult.success(menu);
    }
}
