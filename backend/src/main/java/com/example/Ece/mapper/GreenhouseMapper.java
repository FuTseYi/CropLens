package com.example.Ece.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Ece.entity.Greenhouse;
import org.apache.ibatis.annotations.Select;

public interface GreenhouseMapper extends BaseMapper<Greenhouse> {
    @Select("select * from greenhouse where greenhouse_name = #{greenhouseName}")
    Greenhouse selectByGreenhouseName(String greenhouseName);
} 