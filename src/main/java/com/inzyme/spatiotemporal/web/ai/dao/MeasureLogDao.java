package com.inzyme.spatiotemporal.web.ai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inzyme.spatiotemporal.web.ai.domain.entity.MeasureLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: MeasureLogDao    
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2020年3月10日 下午3:30:52    
 *     
 * @author  Q.JI
 * @version  
 * @since   JDK 1.8
 */
@Component
@Mapper
public interface MeasureLogDao extends BaseMapper<MeasureLog>{

}
