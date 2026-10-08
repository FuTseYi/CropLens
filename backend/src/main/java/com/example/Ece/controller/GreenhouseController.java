package com.example.Ece.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Ece.common.Result;
import com.example.Ece.entity.Greenhouse;
import com.example.Ece.mapper.GreenhouseMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 温室管理控制器
 */
@RestController
@RequestMapping("/greenhouse")
public class GreenhouseController {

    @Resource
    private GreenhouseMapper greenhouseMapper;

    /**
     * 获取所有温室信息
     */
    @GetMapping("/all")
    public Result<?> getAll() {
        return Result.success(greenhouseMapper.selectList(null));
    }

    /**
     * 根据ID获取温室信息
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Integer id) {
        return Result.success(greenhouseMapper.selectById(id));
    }

    /**
     * 分页查询温室信息
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "") String search,
                             @RequestParam(defaultValue = "") String cropType,
                             @RequestParam(defaultValue = "") String growthStatus,
                             @RequestParam(defaultValue = "") String manager,
                             @RequestParam(defaultValue = "") String recordTime) {
        LambdaQueryWrapper<Greenhouse> wrapper = Wrappers.<Greenhouse>lambdaQuery();
        wrapper.orderByDesc(Greenhouse::getCreateTime);
        
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Greenhouse::getGreenhouseName, search);
        }
        if (StrUtil.isNotBlank(cropType)) {
            wrapper.like(Greenhouse::getCropType, cropType);
        }
        if (StrUtil.isNotBlank(growthStatus)) {
            wrapper.like(Greenhouse::getGrowthStatus, growthStatus);
        }
        if (StrUtil.isNotBlank(manager)) {
            wrapper.like(Greenhouse::getManager, manager);
        }
        if (StrUtil.isNotBlank(recordTime)) {
            wrapper.like(Greenhouse::getRecordTime, recordTime);
        }
        
        Page<Greenhouse> greenhousePage = greenhouseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(greenhousePage);
    }

    /**
     * 新增温室信息
     */
    @PostMapping
    public Result<?> save(@RequestBody Greenhouse greenhouse) {
        // 检查温室名称是否重复
        Greenhouse existingGreenhouse = greenhouseMapper.selectByGreenhouseName(greenhouse.getGreenhouseName());
        if (existingGreenhouse != null) {
            return Result.error("-1", "温室名称已存在");
        }
        
        greenhouse.setCreateTime(LocalDateTime.now());
        greenhouse.setUpdateTime(LocalDateTime.now());
        greenhouseMapper.insert(greenhouse);
        return Result.success();
    }

    /**
     * 更新温室信息
     */
    @PostMapping("/update")
    public Result<?> update(@RequestBody Greenhouse greenhouse) {
        // 检查温室名称是否重复（排除自身）
        LambdaQueryWrapper<Greenhouse> wrapper = Wrappers.<Greenhouse>lambdaQuery()
                .eq(Greenhouse::getGreenhouseName, greenhouse.getGreenhouseName())
                .ne(Greenhouse::getId, greenhouse.getId());
        Greenhouse existingGreenhouse = greenhouseMapper.selectOne(wrapper);
        if (existingGreenhouse != null) {
            return Result.error("-1", "温室名称已存在");
        }
        
        greenhouse.setUpdateTime(LocalDateTime.now());
        greenhouseMapper.updateById(greenhouse);
        return Result.success();
    }

    /**
     * 删除温室信息
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        greenhouseMapper.deleteById(id);
        return Result.success();
    }
} 