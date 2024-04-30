package com.example.windy.service.impl;

import com.example.windy.service.AccountTCCService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountTCCServiceImpl implements AccountTCCService {
    @Override
    public void deduct(String userId, int money) {

    }

    @Override
    public boolean confirm(BusinessActionContext context) {
        return false;
    }

    @Override
    public boolean cancel(BusinessActionContext context) {
        return false;
    }
}
