package com.example.windy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.windy.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
