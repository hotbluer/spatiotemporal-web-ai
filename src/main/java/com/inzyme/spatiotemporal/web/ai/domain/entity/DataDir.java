package com.inzyme.spatiotemporal.web.ai.domain.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inzyme.spatiotemporal.web.core.domain.Treeable;
import com.inzyme.spatiotemporal.web.core.domain.entity.CommonEntity;

import lombok.Data;

@TableName("exam_data_dir")
@Data
public class DataDir extends CommonEntity implements Treeable<DataDir>, Serializable {

	private static final long serialVersionUID = 8461777881326370230L;

	@JsonSerialize(using = ToStringSerializer.class)
	private Long parentId;
	
	private String dir;
	
	private String name;
	
	private String description;
	
	private transient List<DataDir> children;
	
}
