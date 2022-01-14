package com.inzyme.spatiotemporal.web.ai.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inzyme.spatiotemporal.web.core.constants.enums.ReturnEnum;
import com.inzyme.spatiotemporal.web.core.domain.vo.ReturnVO;
import com.inzyme.spatiotemporal.web.ai.domain.entity.DataDir;
import com.inzyme.spatiotemporal.web.ai.domain.vo.DataDirVO;
import com.inzyme.spatiotemporal.web.ai.service.IDataDirService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/exam/datadir")
@Api(tags = "范例：数据资源目录处理")
public class DataDirController {
	
	@Autowired
	private IDataDirService dataDirService;
	
	@ApiOperation(value = "新增数据资源目录", notes = "无")
	@PostMapping("/add")
	@Transactional
	public ReturnVO<Void> addDir(@RequestBody @ApiParam("数据目录VO") DataDirVO dataDirVO) {
		// 从VO复制到entity
		DataDir dir = new DataDir();
		BeanUtils.copyProperties(dataDirVO, dir);
		// 调用默认save方法
		dataDirService.save(dir);
		return new ReturnVO<>(ReturnEnum.SUCCESS);
	}
	
	@ApiOperation(value = "批量新增数据资源目录", notes = "无")
	@PostMapping("/addBatch")
	@Transactional
	public ReturnVO<Void> addDirs(@RequestBody @ApiParam("数据目录VO列表") List<DataDirVO> voList) {
		// 从VO复制到entity
		List<DataDir> dirs = new ArrayList<DataDir>();
		for (DataDirVO vo : voList) {
			DataDir dir = new DataDir();
			BeanUtils.copyProperties(vo, dir);
			dirs.add(dir);
		}
		// 调用默认save方法
		dataDirService.saveBatch(dirs);
		return new ReturnVO<>(ReturnEnum.SUCCESS);
	}
	
	@ApiOperation(value = "修改数据资源目录", notes = "无")
	@PostMapping("/update")
	@Transactional
	public ReturnVO<Void> updateDir(@RequestBody @ApiParam("数据目录VO") DataDirVO dataDirVO) {
		// 从VO复制到entity
		DataDir dir = new DataDir();
		BeanUtils.copyProperties(dataDirVO, dir);
		// 调用默认update方法
		dataDirService.updateById(dir);
		return new ReturnVO<>(ReturnEnum.SUCCESS);
	}
	
	@ApiOperation(value = "批量删除数据资源目录", notes = "无")
	@PostMapping("/delete")
	@Transactional
	public ReturnVO<Void> deleteDir(@RequestBody @ApiParam("数据目录VO") List<Long> ids) {
		// 调用默认批量删除方法
		dataDirService.removeByIds(ids);
		return new ReturnVO<>(ReturnEnum.SUCCESS);
	}
	
	@ApiOperation(value = "查询数据资源目录树", notes = "无")
	@PostMapping("/trees")
	public ReturnVO<List<DataDir>> getAllTrees() {
		// 获取全部的目录树
		List<DataDir> trees = dataDirService.getAllTrees();
		return new ReturnVO<List<DataDir>>(ReturnEnum.SUCCESS, trees);
	}

	@ApiOperation(value = "查询下一级数据资源目录", notes = "无")
	@PostMapping("/sublist")
	public ReturnVO<List<DataDir>> getSublist(@RequestParam @ApiParam(value = "当前页面",example = "1") Long parentId) {
		// 获取下一级目录
		List<DataDir> trees = dataDirService.queryByParent(parentId);
		return new ReturnVO<List<DataDir>>(ReturnEnum.SUCCESS, trees);
	}

	@ApiOperation(value = "查询数据资源目录", notes = "无")
	@PostMapping("/page")
	public ReturnVO<IPage<DataDir>> queryByPage(@RequestParam @ApiParam(value="当前页面", defaultValue="1",example = "1") long current,
			@RequestParam @ApiParam(value="每页记录数", defaultValue="10",example = "1") long size,
			@RequestBody @ApiParam("查询条件") DataDirVO dataDirVO) {
		IPage<DataDir> page = dataDirService.queryPage(dataDirVO, new Page<DataDir>(current, size));
		return new ReturnVO<IPage<DataDir>>(ReturnEnum.SUCCESS, page);
	}
}
