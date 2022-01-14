package com.inzyme.spatiotemporal.web.ai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**   
 * @ClassName: MeasureResultDao    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月8日 下午2:39:31    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
*/
@Component
@Mapper
public interface MeasureResultDao extends BaseMapper<MeasureResult>{
	/**
	 * 
	 * @Title: queryByImgId    
	 * @Description: 据imgId取测算结果
	 * @param imgId
	 * @return  
	 * MeasureResult
	 */
	public MeasureResult queryByImgId(String imgId);
	
	/**
	 * 
	 * @Title: updateForImgId    
	 * @Description: 据imgId更新结果
	 * @param para
	 * @return  
	 * int
	 */
	public int updateForImgId(Map para);
}
