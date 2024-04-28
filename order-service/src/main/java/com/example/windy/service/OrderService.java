package com.example.windy.service;


import com.example.windy.entity.Order;

public interface OrderService {

    /**
     * 创建订单
     */
    Long create(Order order);
}