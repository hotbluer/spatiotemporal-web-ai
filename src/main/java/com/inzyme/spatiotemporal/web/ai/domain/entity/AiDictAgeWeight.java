package com.inzyme.spatiotemporal.web.ai.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.inzyme.spatiotemporal.web.core.domain.entity.CommonEntity;

import lombok.Data;

/**   
 * @ClassName: AiDictAgeWeight    
 * @Description: 牲蓄日龄-体重词典表
 * @date 2020年3月8日 下午1:08:27    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@TableName("ai_dict_age_weight")
@Data
public class AiDictAgeWeight extends CommonEntity implements Serializable{/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 3626135253463584919L;
	
	private String mainType;
	
	private Integer age;
	
	private Double weight;

}
