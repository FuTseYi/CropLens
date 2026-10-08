package com.example.Ece.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Ece.common.Result;
import com.example.Ece.entity.Storage;
import com.example.Ece.mapper.StorageMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 库存管理控制器
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Resource
    private StorageMapper storageMapper;

    /**
     * 获取所有库存信息
     */
    @GetMapping("/all")
    public Result<?> getAll() {
        return Result.success(storageMapper.selectList(null));
    }

    /**
     * 根据ID获取库存信息
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(storageMapper.selectById(id));
    }

    /**
     * 分页查询库存信息
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "") String search,
                             @RequestParam(defaultValue = "") String warehouse,
                             @RequestParam(defaultValue = "") String storageArea,
                             @RequestParam(defaultValue = "") String manager,
                             @RequestParam(defaultValue = "") String phone) {
        LambdaQueryWrapper<Storage> wrapper = Wrappers.<Storage>lambdaQuery();
        wrapper.orderByAsc(Storage::getId);
        
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Storage::getProduct, search);
        }
        if (StrUtil.isNotBlank(warehouse)) {
            wrapper.like(Storage::getWarehouse, warehouse);
        }
        if (StrUtil.isNotBlank(storageArea)) {
            wrapper.like(Storage::getStorageArea, storageArea);
        }
        if (StrUtil.isNotBlank(manager)) {
            wrapper.like(Storage::getManager, manager);
        }
        if (StrUtil.isNotBlank(phone)) {
            wrapper.like(Storage::getPhone, phone);
        }
        
        Page<Storage> storagePage = storageMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(storagePage);
    }

    /**
     * 新增库存信息
     */
    @PostMapping
    public Result<?> save(@RequestBody Storage storage) {
        storage.setCreateTime(LocalDateTime.now());
        storage.setUpdateTime(LocalDateTime.now());
        storageMapper.insert(storage);
        return Result.success();
    }

    /**
     * 更新库存信息
     */
    @PostMapping("/update")
    public Result<?> update(@RequestBody Storage storage) {
        storage.setUpdateTime(LocalDateTime.now());
        storageMapper.updateById(storage);
        return Result.success();
    }

    /**
     * 删除库存信息
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        storageMapper.deleteById(id);
        return Result.success();
    }
} 