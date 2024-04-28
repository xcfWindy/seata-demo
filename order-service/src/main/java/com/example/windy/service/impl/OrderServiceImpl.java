package com.example.windy.service.impl;


import com.example.windy.client.AccountClient;
import com.example.windy.client.StorageClient;
import com.example.windy.entity.Order;
import com.example.windy.mapper.OrderMapper;
import com.example.windy.service.OrderService;
import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AccountClient accountClient;
    @Autowired
    private StorageClient storageClient;
    @Autowired
    private OrderMapper orderMapper;


    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public Long create(Order order) {
        // 创建订单
        orderMapper.insert(order);
        try {
            // 扣用户余额
            accountClient.deduct(order.getUserId(), order.getMoney());
            // 扣库存
            storageClient.deduct(order.getCommodityCode(), order.getCount());

        } catch (FeignException e) {
            log.error("下单失败，原因:{}", e.contentUTF8(), e);
            throw new RuntimeException(e.contentUTF8(), e);
        }
        return order.getId();
    }
}
