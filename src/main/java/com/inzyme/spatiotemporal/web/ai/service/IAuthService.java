package com.inzyme.spatiotemporal.web.ai.service;

import com.inzyme.spatiotemporal.web.ai.domain.bean.ResponseTokenBean;

/**   
 * @ClassName: IAuthService    
 * @Description: 鉴权接口
 * @date 2020年3月7日 下午1:29:50    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
public interface IAuthService {
	/**
	 * 
	 * @Title: getAuth    
	 * @Description: AI服务专用鉴权，得到token
	 * @param ak
	 * @param sk
	 * @return  
	 * ResponseTokenBean
	 */
	public ResponseTokenBean getAuth(String ak, String sk);
	
	/**
	 * 
	 * @Title: getAuth    
	 * @Description: 通用鉴权，得到token
	 * @param appType
	 * @param ak
	 * @param sk
	 * @return  
	 * ResponseTokenBean
	 */
	public ResponseTokenBean getAuth(String appType, String ak, String sk);
}
