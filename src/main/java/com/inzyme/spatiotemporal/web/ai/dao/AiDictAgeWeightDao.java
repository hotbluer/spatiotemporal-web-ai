package com.inzyme.spatiotemporal.web.ai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inzyme.spatiotemporal.web.ai.domain.entity.AiDictAgeWeight;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**   
 * @ClassName: AiDictAgeWeightDao    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月8日 下午1:30:07    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Component
@Mapper
public interface AiDictAgeWeightDao extends BaseMapper<AiDictAgeWeight>{
	public List<AiDictAgeWeight> queryByMainTypeWeight(String mainType, Double weight);
}
