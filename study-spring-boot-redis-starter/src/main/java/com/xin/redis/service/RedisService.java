package com.xin.redis.service;

import com.xin.redis.enums.ExpireEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * RedisService
 *
 * @author lemon 2019/11/24 13:15
 * @version V1.0.0
 **/
@Component
public class RedisService {

    /**
     * 日志记录
     */
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    /**
     * redis模板
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间（秒）
     * @return 结果
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis异常", e);
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键
     * @return 时间（秒）返回0表示永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return 结果
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("redis异常", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 键
     */
    public void del(String... keys) {
        List<String> keyList = new ArrayList<>();
        Collections.addAll(keyList, keys);
        redisTemplate.delete(keyList);
    }

    //=========String封账方法============

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存存入
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis异常", e);
            return false;
        }
    }

    /**
     * 普通缓存存入并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒） 不大于0则表示无期限
     * @return 结果
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time <= 0) {
                redisTemplate.opsForValue().set(key, value);
            } else {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis异常", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key 键
     * @param num 递增数量（大于0）
     * @return 结果
     */
    public long incr(String key, long num) {
        Assert.isTrue(num > 0, "递增因子必须大于0！");
        return redisTemplate.opsForValue().increment(key, num);
    }

    /**
     * 递减
     *
     * @param key 键
     * @param num 递减数量（小于0）
     * @return 结果
     */
    public long decr(String key, long num) {
        Assert.isTrue(num < 0, "递减因子必须小于0！");
        return redisTemplate.opsForValue().decrement(key, num);
    }

    //=========hash封账方法============

    /**
     * 获取hashkey中某个字段所对应的值
     *
     * @param key  键 不能为空
     * @param item 项 不能为空
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 根据key获取所有键值
     *
     * @param key 键
     * @return map
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 多键值存入
     *
     * @param key 键
     * @param map map
     * @return 结果
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 多键值存入并设置过期时间
     *
     * @param key  键
     * @param map  map
     * @param time 时间（秒）
     * @return 结果
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * hashset存储单值
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return 结果
     */
    public boolean hset(String key, String item, String value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * hashset存储单值并设置过期时间
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间（秒）注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return 结果
     */
    public boolean hset(String key, String item, String value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以为多个，不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为空
     * @param item 项 不能为空
     * @return 结果
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param num  递增因子
     * @return 值
     */
    public double hincr(String key, String item, double num) {
        return redisTemplate.opsForHash().increment(key, item, num);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param num  递减因子
     * @return 值
     */
    public double hdecr(String key, String item, double num) {
        return redisTemplate.opsForHash().increment(key, item, num);
    }

    //=========Set封账方法============

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return set
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 数量
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return 集合
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 结果
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 将list放入缓存并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 结果
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 结果
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            logger.error("redis操作异常", e);
            return null;
        }
    }

    /**
     * 模糊查询获取key值
     *
     * @param pattern 匹配
     * @return key列表
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     *
     * @param channel 管道
     * @param message 消息内容
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=========BoundListOperations 用法 start============

    /**
     * 将数据添加到Redis的list中（从右边添加）
     *
     * @param listKey    key
     * @param expireEnum 有效期的枚举类
     * @param values     待添加的数据
     */
    public void addToListRight(String listKey, ExpireEnum expireEnum, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    /**
     * 根据起始结束序号遍历Redis中的list
     *
     * @param listKey key
     * @param start   起始序号
     * @param end     结束序号
     * @return 列表
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }

    /**
     * 弹出右边的值 --- 并且移除这个值
     *
     * @param listKey key
     */
    public Object rightPop(String listKey) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }
}

