package com.g0y.auth.component.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * REDIS Service
 */
@Slf4j
@Service
public class RedisService {

  /** Timeout Seconds */
  @Value("${redis.expire.seconds:1800}")
  protected Long expireSeconds;

  /** RedisTemplate */
  @Autowired
  protected RedisTemplate<String, Object> redisTemplate;

  /** ObjectMapper */
  @Autowired
  protected ObjectMapper objectMapper;

  /**
   * Get Data From REDIS
   * 
   * @param key String
   * @return Object
   */
  public Object get(final String key) {
    return this.redisTemplate.opsForValue().get(key);
  }

  /**
   * Get Data From REDIS
   * 
   * @param key String
   * @return Object
   */
  public <T> T get(final String key, Class<T> valueType) {
    Object value = this.get(key);
    T obj = null;
    try {
      String json = this.objectMapper.writeValueAsString(value);
      obj = this.objectMapper.readValue(json, valueType);
    } catch (JsonProcessingException e) {
      log.error("get JsonProcessingException : {}", e);
    }
    return obj;
  }

  /**
   * Set Data To REDIS<br>
   * 有預設Timeout時間
   * 
   * @param key String
   * @param value Object
   */
  public void valueSet(final String key, final Object value) {
    this.valueSet(key, value, Duration.of(this.expireSeconds, ChronoUnit.SECONDS));
  }

  /**
   * Set Data To REDIS
   * 
   * @param key String
   * @param value Object
   * @param timeout Duration
   */
  public void valueSet(final String key, final Object value, final Duration timeout) {
    this.redisTemplate.opsForValue().set(key, value, timeout);
  }

  /** Get Session Map */
  public Map<Object, Object> hashGet(final String key) {
    return this.redisTemplate.opsForHash().entries(key);
  }

  /**
   * 取得資料
   * 
   * @param key REDIS Key 不可為空
   * @param hashKeys 篩選條件
   * @return {@code List<Object>}
   */
  public List<Object> hashMultiGet(final String key, final Collection<Object> hashKeys) {
    return this.redisTemplate.opsForHash().multiGet(key, hashKeys);
  }

  /**
   * 取得資料
   * 
   * @param key REDIS Key 不可為空
   * @return {@code List<Object>}
   */
  public List<Object> hashValues(final String key) {
    return this.redisTemplate.opsForHash().values(key);
  }

  /**
   * Set Data To REDIS
   * 
   * @param key REDIS Key 不可為空
   * @param hashKey 不可為空
   * @param value 不可為空
   */
  public void hashPut(final String key, final Object hashKey, final Object value) {
    this.redisTemplate.opsForHash().put(key, hashKey, value);
  }

  /**
   * Set Data To REDIS
   * 
   * @param key REDIS Key 不可為空
   * @param hashFields 不可為空
   */
  public void hashPutAll(final String key, final Map<?, ?> hashFields) {
    this.redisTemplate.opsForHash().putAll(key, hashFields);
  }

  /**
   * Delete Data
   * 
   * @param keys String[]
   */
  public void delete(final String... keys) {
    Arrays.asList(keys).stream().forEach(key -> {
      this.redisTemplate.delete(key);
    });
  }

  /**
   * By Key 重設REDIS過期時間時間
   */
  public void resetTimeout(final String key, final Long timeout, final TimeUnit timeUnit) {
    this.redisTemplate.expire(key, timeout, timeUnit);
  }

  /**
   * 資料是否存在
   * 
   * @param key REDIS Key 不可為空
   */
  public boolean hasKey(final String key) {
    return this.redisTemplate.hasKey(key);
  }

  /**
   * 取得資料長度
   * 
   * @param key REDIS Key 不可為空
   */
  public long getSize(final String key) {
    return this.redisTemplate.opsForHash().size(key);
  }
}
