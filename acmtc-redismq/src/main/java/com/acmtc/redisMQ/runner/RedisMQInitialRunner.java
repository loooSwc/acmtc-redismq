package com.acmtc.redisMQ.runner;

import com.acmtc.redisMQ.config.RedisAnnotationProcessor;
import com.acmtc.redisMQ.config.RedisMQConfig;
import com.acmtc.redisMQ.pool.RedisMQInitialPool;
import com.acmtc.redisMQ.thread.RedisMQInitialThread;
import com.acmtc.redisMQ.util.RedisMQJedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName RedisMQInitialRunner
 * @Description TODO
 * @Author paul
 * @Date 2018/9/29 17:44
 * @Version 1.0
 */
@Component
public class RedisMQInitialRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(RedisMQInitialRunner.class);
    @Autowired
    private RedisMQConfig redisMQConfig;
    @Autowired
    private RedisMQJedisUtil redisMQJedisUtil;

    @Override
    public void run (ApplicationArguments var) {
        if (redisMQConfig.isInitial()) {
            log.info("RedisMQInitialRunner start!");
            Map<String,Object> maps = RedisAnnotationProcessor.EVENTCODESERVICEBEANMAP;
            if (null != maps && maps.size() > 0) {
                for (Object consumer : maps.values()) {
                    ExecutorService pool = RedisMQInitialPool.pool;
                    RedisMQInitialThread thread = new RedisMQInitialThread(consumer, redisMQJedisUtil);
                    pool.submit(thread);
                }
            }
            log.info("RedisMQInitialRunner end!");
        }
    }

}