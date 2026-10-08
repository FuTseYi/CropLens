package com.example.Ece.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Ece.common.Result;
import com.example.Ece.entity.Disease;
import com.example.Ece.mapper.DiseaseMapper;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;


@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Resource
    private DiseaseMapper diseaseMapper;

    @PostMapping
    public Result<?> save(@RequestBody Disease disease) {
        diseaseMapper.insert(disease);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Disease disease) {
        diseaseMapper.updateById(disease);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        diseaseMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String name,
                              @RequestParam(defaultValue = "") String cropType) {
        LambdaQueryWrapper<Disease> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Disease::getName, name);
        }
        if (StringUtils.isNotBlank(cropType)) {
            wrapper.like(Disease::getCropType, cropType);
        }
        Page<Disease> diseasePage = diseaseMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(diseasePage);
    }

}