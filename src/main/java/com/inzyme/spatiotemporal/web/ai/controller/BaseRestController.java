package com.inzyme.spatiotemporal.web.ai.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.inzyme.spatiotemporal.web.ai.service.AiRedisService;
import com.inzyme.spatiotemporal.web.ai.util.IpUtils;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName: BaseRestController    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月7日 下午9:49:24    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Slf4j
public abstract class BaseRestController {
	
	@Autowired
	private AiRedisService redis;
	
	/**
	 * 
	 * @Title: verifyAppId    
	 * @Description: 不涉及AI服务能力的appID有效性验证
	 * @param appId
	 * @return  
	 * boolean
	 */
	protected boolean verifyAppId(String appId)  {
		boolean success = false;
		Integer cnt = redis.getAppCallCnt(appId);
		if(null != cnt) {			
			success = true;
		}
		if(!success) {
			log.warn("{}：应用方无访问权限，请联系管理员！", appId);
		}
		return success;
	}
	
	
	/**
	 * 
	 * @Title: verifyAppId    
	 * @Description: 涉及到AI服务能力的appID有效性验证
	 * @param request
	 * @param appId
	 * @return  
	 * int
	 */
	protected int verifyAppId(HttpServletRequest request, String appId, String aiType) {
		int res = 1;	// appid错
		Integer cnt = redis.getAppCallCnt(appId, aiType);
		if(null != cnt) {
			redis.putAppCallCnt(appId, aiType);
			res = 0;
		}
		if(res != 0) {
			log.warn("{}：应用方无 {} 访问权限，请联系管理员！", appId, aiType);
			return res;
		}	
		return checkInterval(request);
	}
	
	/**
	 * 
	 * @Title: checkInterval    
	 * @Description: 请求间隔检查 
	 * @param request
	 * @return  
	 * int
	 */
	protected int checkInterval(HttpServletRequest request) {
		int res = 2;	// 请求频繁错
		String ip = IpUtils.getIpAddr(request);
		String cacheIp = redis.getRequestIp(ip);		
		if(null == cacheIp || cacheIp.length() == 0) {
			redis.putRequestIp(ip);
			res = 0;
		}
		if(res != 0) {
			log.warn("{}：客户端操作过于频繁，请稍后再试。", ip);
		}
		return res;
	}
}
