package com.example.Ece.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Ece.entity.Storage;
import org.apache.ibatis.annotations.Select;

public interface StorageMapper extends BaseMapper<Storage> {
    @Select("select * from storage where product = #{product}")
    Storage selectByProduct(String product);
} 