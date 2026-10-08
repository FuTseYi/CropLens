package com.example.Ece.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Ece.entity.Purchase;
import org.apache.ibatis.annotations.Select;

public interface PurchaseMapper extends BaseMapper<Purchase> {
    @Select("select * from purchase where product_name = #{productName}")
    Purchase selectByProductName(String productName);
} 