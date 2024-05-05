package com.example.windy.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

//@LocalTCC声明一个TCC事务接口
@LocalTCC
public interface AccountTCCService {

    /**
     * @TwoPhaseBusinessAction 声明事务的Try方法
     * name= try的方法名 commitMethod=confirm的方法名 rollbackMethod=cancel的方法名
     * Try逻辑
     * @param userId @BusinessActionContextParameter 声明Try方法的参数，可以加入到TCC事务的上下文中，方便commitMethod，rollbackMethod方法获取
     * @param money @BusinessActionContextParameter 声明Try方法的参数，可以加入到TCC事务的上下文中，方便commitMethod，rollbackMethod方法获取
     */
    @TwoPhaseBusinessAction(name = "deduct",commitMethod = "confirm",rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,
                @BusinessActionContextParameter(paramName = "money") int money);

    /**
     * 二阶段confirm确认方法，需要与commitMethod一致
     * @param context 上下文，可以传递try方法的参数
     * @return 执行是否成功
     */
    boolean confirm(BusinessActionContext context);

    /**
     * 二阶段rollback回滚方法，需要与rollbackMethod一致
     * @param context 上下文，可以传递try方法的参数
     * @return 执行是否成功
     */
    boolean cancel(BusinessActionContext context);
}
