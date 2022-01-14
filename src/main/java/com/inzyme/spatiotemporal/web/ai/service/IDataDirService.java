package com.inzyme.spatiotemporal.web.ai.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.inzyme.spatiotemporal.web.ai.domain.entity.DataDir;
import com.inzyme.spatiotemporal.web.ai.domain.vo.DataDirVO;

/**
 * 范例：数据目录业务逻辑接口
 * @author marquis
 *
 */
public interface IDataDirService extends IService<DataDir>{
	
	/**
	 * 查询全部数据目录树
	 * @return
	 */
	public List<DataDir> getAllTrees();
	
	/**
	 * 查询下一级数据资源目录
	 * @param parentId
	 * @return
	 */
	public List<DataDir> queryByParent(Long parentId);
	
	/**
	 * 按条件分页查询
	 * @param cond 查询条件
	 * @param page 分页条件
	 * @return
	 */
	public IPage<DataDir> queryPage(DataDirVO cond, IPage<DataDir> page);
}
