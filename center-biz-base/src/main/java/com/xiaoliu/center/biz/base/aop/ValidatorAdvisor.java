package com.xiaoliu.center.biz.base.aop;

import com.xiaoliu.center.biz.base.annotation.*;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author xiaoliu
 */
public class ValidatorAdvisor extends AbstractPointcutAdvisor {

    private ValidatorMethodInterceptor interceptor;

    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            if (Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
                return method.isAnnotationPresent(Length.class) || method.isAnnotationPresent(NotNull.class) || method.isAnnotationPresent(NotEmpty.class) || method.isAnnotationPresent(Size.class);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            return false;
        }
    };

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.interceptor;
    }

    public void setAdvice(ValidatorMethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

}
