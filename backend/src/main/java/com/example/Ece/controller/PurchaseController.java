package com.example.Ece.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Ece.common.Result;
import com.example.Ece.entity.Purchase;
import com.example.Ece.mapper.PurchaseMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 农资采购管理控制器
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Resource
    private PurchaseMapper purchaseMapper;

    /**
     * 获取所有采购信息
     */
    @GetMapping("/all")
    public Result<?> getAll() {
        return Result.success(purchaseMapper.selectList(null));
    }

    /**
     * 根据ID获取采购信息
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(purchaseMapper.selectById(id));
    }

    /**
     * 分页查询采购信息
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "") String search,
                             @RequestParam(defaultValue = "") String supplier,
                             @RequestParam(defaultValue = "") String region,
                             @RequestParam(defaultValue = "") String manager) {
        LambdaQueryWrapper<Purchase> wrapper = Wrappers.<Purchase>lambdaQuery();
        wrapper.orderByDesc(Purchase::getCreateTime);
        
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Purchase::getProductName, search);
        }
        if (StrUtil.isNotBlank(supplier)) {
            wrapper.like(Purchase::getSupplier, supplier);
        }
        if (StrUtil.isNotBlank(region)) {
            wrapper.like(Purchase::getRegion, region);
        }
        if (StrUtil.isNotBlank(manager)) {
            wrapper.like(Purchase::getManager, manager);
        }
        
        Page<Purchase> purchasePage = purchaseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(purchasePage);
    }

    /**
     * 新增采购信息
     */
    @PostMapping
    public Result<?> save(@RequestBody Purchase purchase) {
        purchase.setCreateTime(LocalDateTime.now());
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseMapper.insert(purchase);
        return Result.success();
    }

    /**
     * 更新采购信息
     */
    @PostMapping("/update")
    public Result<?> update(@RequestBody Purchase purchase) {
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseMapper.updateById(purchase);
        return Result.success();
    }

    /**
     * 删除采购信息
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        purchaseMapper.deleteById(id);
        return Result.success();
    }
} 