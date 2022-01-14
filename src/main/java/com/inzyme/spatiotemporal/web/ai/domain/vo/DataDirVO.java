package com.inzyme.spatiotemporal.web.ai.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "数据资源目录")
@Data
public class DataDirVO {
	
	@ApiModelProperty(value = "目录ID")
	private Integer id;

	@ApiModelProperty(value = "目录名(英文)")
	private String dir;
	
	@ApiModelProperty(value = "目录名(中文)")
	private String name;
	
	@ApiModelProperty(value = "备注")
	private String description;
	
	@ApiModelProperty(value = "父级目录ID")
	private Integer paraentId;
}
