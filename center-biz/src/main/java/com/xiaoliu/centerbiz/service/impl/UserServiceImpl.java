package com.xiaoliu.centerbiz.service.impl;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.xiaoliu.centerbiz.common.annotation.NotEmpty;
import com.xiaoliu.centerbiz.common.result.Result;
import com.xiaoliu.centerbiz.dao.IUserDao;
import com.xiaoliu.centerbiz.domain.User;
import com.xiaoliu.centerbiz.domain.enumeration.UserStatusEnum;
import com.xiaoliu.centerbiz.domain.enumeration.UserStatusTriggerEnum;
import com.xiaoliu.centerbiz.service.IUserService;
import com.xiaoliu.centerbiz.service.conver.UserStatusConver;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;

    private static StateMachine<UserStatusEnum, UserStatusTriggerEnum> stateMachine = new StateMachine<>(UserStatusEnum.A, UserStatusConver.config);

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    @NotEmpty(name = {"username"}, messages = {"用户名不能为空"})
    public Result updateUser(User user) {
        /*UserStatusEnum a = UserStatusEnum.A;
        UserStatusEnum b = UserStatusEnum.B;
        UserStatusTriggerEnum x = UserStatusTriggerEnum.X;

        StateMachineConfig<UserStatusEnum, UserStatusTriggerEnum> config = new StateMachineConfig<>();

        config.configure(a).permit(x, b);

        StateMachine<UserStatusEnum, UserStatusTriggerEnum> sm = new StateMachine<>(a, config);
        sm.fire(x);

        if (b.equals(sm.getState())) {
            System.out.println("状态机校验成功");
            return new Result(true, "状态机校验成功");
        } else {
            System.out.println("状态机校验失败");
            return new Result(false, "状态机校验失败");
        }*/

        stateMachine.fire(UserStatusTriggerEnum.Y);
        System.out.println("userStatus-->" + stateMachine.getState());
        return new Result(true, stateMachine.getState() + "");
    }
}
