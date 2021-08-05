package com.study.common.config;

import com.study.common.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * redis 实现分布式锁
 */
@Component
@Slf4j
public class RedisLock {
    //超时时间10s
    private static final int TIMEOUT = 10 * 1000;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间 + 超时时间
     * @return
     */
    public boolean lock(String key, String value){
        if (redisTemplate.opsForValue().setIfAbsent(key, value)){
            return true;
        }

        //解决死锁，且当多个线程同时来时，只会让一个线程拿到锁
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果过期
        if (!StringUtils.isEmpty(currentValue) &&
                Long.parseLong(currentValue) < System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value){

        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }catch (Exception e){
            log.error("【redis锁】解锁失败, {}", e);
        }
    }

    /**
     * 模拟秒杀
     */
    public void secKill(String productId){
        long time = System.currentTimeMillis() + TIMEOUT;
        //加锁
        if (!lock(productId, String.valueOf(time))){
            throw new MyException("人太多了，等会儿再试吧~",101);
        }
        //具体的秒杀逻辑

        //解锁
        unlock(productId, String.valueOf(time));
    }
}
