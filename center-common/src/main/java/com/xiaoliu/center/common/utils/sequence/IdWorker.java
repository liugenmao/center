package com.xiaoliu.center.common.utils.sequence;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 重庆渝欧跨境电子商务有限公司
 * ========================
 * Author: dyang
 * Date: 2017/6/25
 * Time: 23:07
 * Describe: 类描述
 */
@UtilityClass
@Slf4j
public class IdWorker {

    private static final Sequence worker;

    static {
        long workerId = ThreadLocalRandom.current().nextInt(31);
        long datacenterId = ThreadLocalRandom.current().nextInt(31);
        log.info("workerId={},datacenterId={}", workerId, datacenterId);
        worker = new Sequence(workerId, datacenterId);
    }

    /**
     * 主机和进程的机器码
     */
    public static long getId() {
        return worker.nextId();
    }

    /**
     * <p>
     * 获取去掉"-" UUID
     * </p>
     */
    public static synchronized String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

//    public static void main(String[] args) {
//        Sequence  worker = new Sequence(1, 2);
//        Sequence  worker2 = new Sequence(1, 3);
//        System.out.println(worker.nextId());
//        System.out.println(worker.nextId());
//        System.out.println(worker.nextId());
//        System.out.println(worker2.nextId());
//        System.out.println(worker2.nextId());
//        System.out.println(worker2.nextId());
//    }

}
