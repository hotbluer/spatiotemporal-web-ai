package com.inzyme.spatiotemporal.web.ai.domain.bean;

import lombok.Data;

/**   
 * @ClassName: ResponseTokenBean    
 * @Description: 鉴权结果返回
 * @date 2020年3月7日 下午1:35:51    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Data
public class ResponseTokenBean {
	private Integer expires_in;
	private String access_token;
	private String expiresDate;
}
