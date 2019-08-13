package com.xiaoliu.center.biz.base.annotation;

import com.xiaoliu.centerbiz.common.exception.LogicException;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务系统事务管理器
 *
 * @author xiaoliu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(value = "centerTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = {LogicException.class, RuntimeException.class})
public @interface CenterTransactional {

    /**
     * 事务传递性，请参考{@link Transactional#propagation()}
     *
     * @return
     */
    @AliasFor(annotation = Transactional.class, attribute = "propagation")
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * 事务隔离性，请参考{@link Transactional#isolation()}
     *
     * @return
     */
    @AliasFor(annotation = Transactional.class, attribute = "isolation")
    Isolation isolation() default Isolation.DEFAULT;

    /**
     * 事务超时时间，默认无超时时间
     *
     * @return
     */
    @AliasFor(annotation = Transactional.class, attribute = "timeout")
    int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

    /**
     * 事务是否只具备只读性
     *
     * @return
     */
    @AliasFor(annotation = Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;
}