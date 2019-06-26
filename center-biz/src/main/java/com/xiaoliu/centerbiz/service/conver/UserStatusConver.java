package com.xiaoliu.centerbiz.service.conver;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.xiaoliu.centerbiz.domain.enumeration.UserStatusEnum;
import com.xiaoliu.centerbiz.domain.enumeration.UserStatusTriggerEnum;

public class UserStatusConver {

    public static StateMachineConfig<UserStatusEnum, UserStatusTriggerEnum> config = new StateMachineConfig<>();

    static {
        config.configure(UserStatusEnum.A)
                .permit(UserStatusTriggerEnum.X, UserStatusEnum.B)
                .permit(UserStatusTriggerEnum.Y, UserStatusEnum.C)
                .permit(UserStatusTriggerEnum.Z, UserStatusEnum.D);
        config.configure(UserStatusEnum.B)
                .ignore(UserStatusTriggerEnum.X)
                .permit(UserStatusTriggerEnum.Y, UserStatusEnum.C)
                .permit(UserStatusTriggerEnum.Z, UserStatusEnum.D);
        config.configure(UserStatusEnum.C)
                .ignore(UserStatusTriggerEnum.X)
                .ignore(UserStatusTriggerEnum.Y)
                .permit(UserStatusTriggerEnum.Z, UserStatusEnum.D);
        config.configure(UserStatusEnum.D)
                .ignore(UserStatusTriggerEnum.X)
                .ignore(UserStatusTriggerEnum.Y)
                .ignore(UserStatusTriggerEnum.Z);
    }
}
