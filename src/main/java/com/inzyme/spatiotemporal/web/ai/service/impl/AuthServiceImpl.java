package com.inzyme.spatiotemporal.web.ai.service.impl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inzyme.spatiotemporal.core.async.util.DateUtils;
import com.inzyme.spatiotemporal.core.async.util.DateUtils.DateFormatter;
import com.inzyme.spatiotemporal.web.ai.config.SysConfig;
import com.inzyme.spatiotemporal.web.ai.domain.bean.ResponseTokenBean;
import com.inzyme.spatiotemporal.web.ai.service.AiRedisService;
import com.inzyme.spatiotemporal.web.ai.service.IAuthService;
import com.inzyme.spatiotemporal.web.ai.util.AiConstants;
import com.inzyme.spatiotemporal.web.ai.util.GsonUtils;
import com.inzyme.spatiotemporal.web.ai.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName: AuthServiceImpl    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月7日 下午1:34:09    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService{
	
	@Autowired
	private SysConfig syscfg;
	
	@Autowired
	private AiRedisService redis;

	/*
	 * (non-Javadoc)
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAuthService#getAuth(java.lang.String, java.lang.String)
	 */
	@Override
	public ResponseTokenBean getAuth(String ak, String sk) {        
        return getAuth(AiConstants.AITOKEN_BDAPPKEY, ak, sk);
	}

	/* (non-Javadoc)
	 * @see com.inzyme.spatiotemporal.web.ai.service.IAuthService#getAuth(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ResponseTokenBean getAuth(String appType, String ak, String sk) {		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("grant_type", "client_credentials");
        map.put("client_id", ak);
        map.put("client_secret", sk);    
        
        ResponseTokenBean tokenBean = null;
        boolean retake = false;
        
        try {
        	tokenBean = redis.getMeasureToken(appType);
        	if(null != tokenBean) {
        		LocalDateTime now = LocalDateTime.now();
        		LocalDateTime expiresDate = DateUtils.toLocalDateTime(DateUtils.parse(tokenBean.getExpiresDate(), DateFormatter.YYYYMMDDHHMMSS_FORMATTER));
        		if(now.isAfter(expiresDate)) {	// 过期
        			retake = true;
        		}
        	}else {
        		retake = true;
        	}
        	
        	if(retake) {
            	String result = HttpUtil.get(syscfg.getAuthHost(), map);  
            	tokenBean = GsonUtils.fromJson(result, ResponseTokenBean.class);
            	if(null != tokenBean) {        		
            		String expiresDate = DateUtils.increase(new Date(), Calendar.SECOND, (tokenBean.getExpires_in() - 1800), DateFormatter.YYYYMMDDHHMMSS_FORMATTER);
            		tokenBean.setExpiresDate(expiresDate);
            		redis.putMeasureToken(appType, tokenBean);
            	}
        	}         	
        }catch (Exception e) {
        	log.error("获取token失败！", e);
        }
		return tokenBean;
	}
}
