package com.example.windy.service.impl;

import com.example.windy.entity.AccountFreezeTbl;
import com.example.windy.mapper.AccountFreezeTblMapper;
import com.example.windy.mapper.AccountMapper;
import com.example.windy.service.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Slf4j
@Service
public class AccountTCCServiceImpl implements AccountTCCService {

    @Autowired
    private AccountFreezeTblMapper freezeTblMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        log.info("开始TCCTry");
        //0.获取全局事务id
        String xid = RootContext.getXID();
        //1.判断是否业务悬挂，即try方法执行前已经存在冻结记录了,存在记录说明cancel执行过了，就不需要执行try了
        AccountFreezeTbl oldFreeze = freezeTblMapper.selectById(xid);
        if (!ObjectUtils.isEmpty(oldFreeze)){
            return;
        }
        //1.扣减可用余额
        accountMapper.deduct(userId,money);
        //2.记录冻结信息
        AccountFreezeTbl accountFreezeTbl = new AccountFreezeTbl();
        accountFreezeTbl.setXid(xid);
        accountFreezeTbl.setUserId(userId);
        accountFreezeTbl.setFreezeMoney(money);
        accountFreezeTbl.setState(AccountFreezeTbl.State.TRY);
        freezeTblMapper.insert(accountFreezeTbl);
    }

    @Override
    public boolean confirm(BusinessActionContext context) {
        //1.获取事务id
        String xid = context.getXid();
        //2.根据id删除冻结记录
        int count = freezeTblMapper.deleteById(xid);

        return count==1;
    }

    @Override
    public boolean cancel(BusinessActionContext context) {
        log.info("开始TCC回滚");
        //0.查询冻结记录
        String xid = context.getXid();
        AccountFreezeTbl accountFreezeTbl = freezeTblMapper.selectById(xid);
        String userId = Objects.requireNonNull(context.getActionContext("userId")).toString();
        //1.空回滚判断，如果当前不存在冻结记录进入了cancel方法，说明try方法没有执行需要空回滚，并利率一条空回滚的冻结信息
        if (ObjectUtils.isEmpty(accountFreezeTbl)){
            accountFreezeTbl=new AccountFreezeTbl();
            accountFreezeTbl.setXid(xid);
            accountFreezeTbl.setFreezeMoney(0);
            accountFreezeTbl.setState(AccountFreezeTbl.State.CANCLE);
            accountFreezeTbl.setUserId(userId);
            return 1==freezeTblMapper.insert(accountFreezeTbl);
        }
        //2.判断幂等性，如果当前冻结信息的状态是cancel，说明cancel执行过了不需要重复执行
        if (AccountFreezeTbl.State.CANCLE==accountFreezeTbl.getState()){
            return true;
        }
        //3.非空回滚操作，恢复原数据
        accountMapper.refund(accountFreezeTbl.getUserId(), accountFreezeTbl.getFreezeMoney());
        //4.将冻结金额清零，改状态为cancel
        accountFreezeTbl.setFreezeMoney(0);
        accountFreezeTbl.setState(AccountFreezeTbl.State.CANCLE);

        return 1==freezeTblMapper.updateById(accountFreezeTbl);
    }
}
