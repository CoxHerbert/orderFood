package com.ruoyi.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.order.domain.Store;
import com.ruoyi.order.service.IStoreService;

/**
 * 门店管理Controller
 */
@RestController
@RequestMapping("/api/admin/store")
public class StoreController extends BaseController
{
    @Autowired
    private IStoreService storeService;

    /**
     * 查询门店列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Store store)
    {
        startPage();
        List<Store> list = storeService.selectStoreList(store);
        return getDataTable(list);
    }

    /**
     * 获取门店详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(storeService.selectStoreById(id));
    }

    /**
     * 新增门店
     */
    @Log(title = "门店", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Store store)
    {
        return toAjax(storeService.insertStore(store));
    }

    /**
     * 修改门店
     */
    @Log(title = "门店", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Store store)
    {
        return toAjax(storeService.updateStore(store));
    }

    /**
     * 删除门店
     */
    @Log(title = "门店", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storeService.deleteStoreByIds(ids));
    }
}
