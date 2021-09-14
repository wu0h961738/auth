package com.g0y.auth.component.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.g0y.auth.component.service.model.AccessTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for Session : 只做驗證用戶，
 * 1. (depreciated: 該server只做單點登入用戶驗證)目前是否在登入狀態(檢查threadLocal)
 * 2. token是否過期(撈redis取accessToken，打verifyToken確認token有沒有過期)
 */
@Slf4j
@Service
public class SessionService {

  /** Timeout Seconds */
  @Value("${redis.expire.seconds:1800}")
  private Long expireSeconds;
  /** RedisService */
  private final RedisService redisService;
  /** ObjectMapper */
  private final ObjectMapper objectMapper;
  /** Token */
  private final ThreadLocal<String> token = new ThreadLocal<>();
  /** AccessTokenInfo */
  private final ThreadLocal<AccessTokenInfo> accessTokenInfo = new ThreadLocal<>();

  /**
   * 建構子 <br>
   * 建立同時取得Header中的Token，用於此次Request <br>
   */
  @Autowired
  public SessionService(RedisService redisService, ObjectMapper objectMapper,
      @Value("${redis.expire.seconds:1800}") Long expireSeconds) {
    super();
    this.redisService = redisService;
    this.expireSeconds = expireSeconds;
    this.objectMapper = new ObjectMapper(objectMapper.getFactory());
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Set AccessTokenInfo To REDIS
   * 
   * @param accessTokenInfo AccessTokenInfo
   */
  @SuppressWarnings("unchecked")
  public void setAccessTokenInfo(String cookieId, final AccessTokenInfo accessTokenInfo) {
    Map<Object, Object> accessTokenInfoMap = this.objectMapper.convertValue(accessTokenInfo, Map.class);
    this.redisService.hashPutAll(cookieId, accessTokenInfoMap);
    this.resetSessionTimeout();
  }

  /**
   * Get AccessTokenInfo
   */
  public AccessTokenInfo getAccessTokenInfo() {
    return this.accessTokenInfo.get();
  }

  /**
   * Create Session
   * 
   * @param token String
   */
  public void newSession(String token) {
    this.token.set(token);
    Map<String, Object> session = new HashMap<>();
    this.redisService.hashPutAll(this.getSessionId(), session);
  }

  /**
   * 重設Session過期時間
   */
  public void resetSessionTimeout() {
    // TODO payload取得expire_in作為參數塞入，指定timeout時間
    this.redisService.resetTimeout(this.getSessionId(), this.expireSeconds, TimeUnit.SECONDS);
  }

  /**
   * 驗證Token是否有效<br>
   * 若有效則順便取得AccessTokenInfo，後面就不再從REDIS取
   * 
   * @return Boolean
   */
  public Boolean checkTokenIsLogin() {
    Boolean isLogin = this.redisService.hasKey(this.getSessionId());
    if (isLogin) {
      this.accessTokenInfo.set(this.getSession());
    }
    return isLogin;
  }

  /** Get Session Map */
  private AccessTokenInfo getSession() {
    AccessTokenInfo accessTokenInfo = null;
    Map<Object, Object> session = this.redisService.hashGet(this.getSessionId());
    try {
      String json = this.objectMapper.writeValueAsString(session);
      accessTokenInfo = this.objectMapper.readValue(json, AccessTokenInfo.class);
    } catch (JsonProcessingException e) {
      log.error("get JsonProcessingException : {}", e);
    }
    return accessTokenInfo;
  }

  /** Get Session Token */
  private String getSessionId() {
    return this.token.get();
  }

  /** Set Token */
  public void setToken(String token) {
    this.token.set(token);
  }

  /**
   * modify data in Redis
   * */
  @Deprecated
  public void setLocalAccessTokenInfo(final AccessTokenInfo info) {
    this.accessTokenInfo.set(info);
  }

  /**
   * Remove ThreadLocal
   */
  public void resetSession() {
    this.token.remove();
    this.accessTokenInfo.remove();
  }
}
