package com.xiaoliu.centerbiz.common.config;

import com.xiaoliu.centerbiz.common.aop.ValidatorAdvisor;
import com.xiaoliu.centerbiz.common.aop.ValidatorMethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xiaoliu
 */
@Configuration
@EnableAspectJAutoProxy
public class RootConfiguration {

    @Bean
    public ProxyFactoryBean validatorPointCut() {
        ProxyFactoryBean pointcut = new ProxyFactoryBean();
        pointcut.addAdvisor(validatorAdvisor());
        return pointcut;
    }

    @Bean
    public ValidatorAdvisor validatorAdvisor() {
        ValidatorMethodInterceptor interceptor = new ValidatorMethodInterceptor();
        ValidatorAdvisor validatorAdvisor = new ValidatorAdvisor();
        validatorAdvisor.setAdvice(interceptor);
        return validatorAdvisor;
    }

}
