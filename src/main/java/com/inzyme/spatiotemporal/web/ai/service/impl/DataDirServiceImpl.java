package com.inzyme.spatiotemporal.web.ai.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inzyme.spatiotemporal.web.core.exception.BaseBusinessException;
import com.inzyme.spatiotemporal.web.core.utils.TreeUtils;
import com.inzyme.spatiotemporal.web.ai.dao.DataDirDao;
import com.inzyme.spatiotemporal.web.ai.domain.entity.DataDir;
import com.inzyme.spatiotemporal.web.ai.domain.vo.DataDirVO;
import com.inzyme.spatiotemporal.web.ai.service.IDataDirService;

import lombok.extern.slf4j.Slf4j;

/**
 * 范例：数据目录业务逻辑
 * @author marquis
 *
 */
@Slf4j
@Service
public class DataDirServiceImpl extends ServiceImpl<DataDirDao, DataDir> implements IDataDirService{

	@Override
	public boolean removeByIds(Collection<? extends Serializable> ids) {
		// 查询子节点
		QueryWrapper<DataDir> wrapper = new QueryWrapper<DataDir>();
		wrapper.in("parent_id", ids);
		if (baseMapper.selectCount(wrapper) > 0) {
			throw new BaseBusinessException("500", "存在子节点，无法删除！");
		}
		return super.removeByIds(ids);
	}
	
	@Override
	public List<DataDir> getAllTrees() {
		List<DataDir> list = baseMapper.selectList(null);
		return TreeUtils.list2Tree(list);
	}

	@Override
	public List<DataDir> queryByParent(Long parentId) {
		// 准备查询条件
		QueryWrapper<DataDir> wrapper = new QueryWrapper<DataDir>();
		if (parentId == null) {
			wrapper.isNull("parent_id");
		} else {
			wrapper.eq("parent_id", parentId);
		}
		return baseMapper.selectList(wrapper);
	}

	@Override
	public IPage<DataDir> queryPage(DataDirVO cond, IPage<DataDir> page) {
		// 准备查询条件
		QueryWrapper<DataDir> wrapper = new QueryWrapper<DataDir>();
		wrapper.apply("1=1");
		if(StringUtils.isNotEmpty(cond.getDir())) {
			wrapper.and(w->w.like("dir", cond.getDir()));
		}
		if(StringUtils.isNotEmpty(cond.getName())) {
			wrapper.and(w->w.like("name", cond.getName()));
		}
		// 按更新时间升序排序
		wrapper.orderByAsc("update_time");

		// 查询
		return page(page, wrapper);
	}
}
