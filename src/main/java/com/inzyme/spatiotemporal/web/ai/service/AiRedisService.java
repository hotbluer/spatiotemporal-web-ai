package com.inzyme.spatiotemporal.web.ai.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.inzyme.spatiotemporal.core.async.util.DateUtils;
import com.inzyme.spatiotemporal.core.async.util.DateUtils.DateFormatter;
import com.inzyme.spatiotemporal.web.ai.config.SysConfig;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ResponseTokenBean;
import com.inzyme.spatiotemporal.web.ai.util.AiConstants;
import com.inzyme.spatiotemporal.web.ai.util.SysUtil;
import com.inzyme.spatiotemporal.web.core.constants.Constants;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName: AiRedisService    
 * @Description: 缓存服务
 * @date 2020年3月8日 下午11:25:19    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Slf4j
@Service
public class AiRedisService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private SysConfig sysConfig;
	
	/**
	 * 
	 * @Title: putMeasureToken    
	 * @Description: 缓存token 
	 * @param aiKey
	 * @param tokenBean  
	 * void
	 */
	public void putMeasureToken(String aiKey, ResponseTokenBean tokenBean) {
		redisTemplate.opsForHash().put(AiConstants.SPATIOTEMPORAL_AI_TOKEN, aiKey, tokenBean);		
	}
	
	/**
	 * 
	 * @Title: getMeasureToken    
	 * @Description: 据AI应用key取得对应token
	 * @param aiKey
	 * @return  
	 * ResponseTokenBean
	 */
	public ResponseTokenBean getMeasureToken(String aiKey) {
		return (ResponseTokenBean) redisTemplate.opsForHash().get(AiConstants.SPATIOTEMPORAL_AI_TOKEN, aiKey);
	}
	
	/**
	 * 
	 * @Title: putAppCallCnt    
	 * @Description: 更新app能力调用数据
	 * @param appId  
	 * void
	 */
	public void putAppCallCnt(String appId, String aiType) {
		Integer cnt = getAppCallCnt(appId, aiType);
		if(null != cnt) {
			redisTemplate.opsForHash().put(AiConstants.SPATIOTEMPORAL_AI_APPID, appId + "|" + aiType, cnt + 1);		
		}
	}
	

	/**
	 * 
	 * @Title: getAppCallCnt    
	 * @Description: 取得app能力调用数据
	 * @param appId
	 * @return  
	 * Integer
	 */
	public Integer getAppCallCnt(String appId, String aiType) {
		return (Integer) redisTemplate.opsForHash().get(AiConstants.SPATIOTEMPORAL_AI_APPID, appId + "|" + aiType);
	}
	
	
	/**
	 * 
	 * @Title: putAppCallCnt    
	 * @Description: 更新app能力调用数据
	 * @param appId  
	 * void
	 */
	public void putAppCallCnt(String appId) {
		Integer cnt = getAppCallCnt(appId);
		if(null != cnt) {
			redisTemplate.opsForHash().put(AiConstants.SPATIOTEMPORAL_AI_APPID, appId, cnt + 1);		
		}
	}
	

	/**
	 * 
	 * @Title: getAppCallCnt    
	 * @Description: 取得app能力调用数据
	 * @param appId
	 * @return  
	 * Integer
	 */
	public Integer getAppCallCnt(String appId) {
		return (Integer) redisTemplate.opsForHash().get(AiConstants.SPATIOTEMPORAL_AI_APPID, appId);
	}
	
	
	/**
	 * 
	 * @Title: getRequestIp    
	 * @Description: 据请求IP取得客户端上次请求时间
	 * @param ip
	 * @return  
	 * String
	 */
	public String getRequestIp(String ip) {
		return SysUtil.nvl(redisTemplate.opsForValue().get(AiConstants.SPATIOTEMPORAL_AI_Expire + ":" + ip));
	}
	
	/**
	 * 
	 * @Title: putRequestIp    
	 * @Description: 据请求IP缓存客户端上次请求时间（缓存时间requestExpire配置项）
	 * @param ip  
	 * void
	 */
	public void putRequestIp(String ip) {
		redisTemplate.opsForValue().set(AiConstants.SPATIOTEMPORAL_AI_Expire + ":" + ip, DateUtils.format(new Date(), DateFormatter.YYYYMMDDHHMMSS_FORMATTER), sysConfig.getRequestExpire(), TimeUnit.SECONDS);
	}
}
