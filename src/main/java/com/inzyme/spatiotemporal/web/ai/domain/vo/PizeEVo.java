package com.inzyme.spatiotemporal.web.ai.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author fyl
 * @since 2021-12-21 17:37
 */
@ApiModel(description= "知长知重返回")
@Data
public class PizeEVo {
    /**
     * 体长cm
     */
    @Excel(name = "体长cm", orderNum = "1", width = 30)
    private Float length;

    /**
     * 体重kg
     */
    @Excel(name = "体重kg", orderNum = "1", width = 30)
    private Double weight;

    /**
     * 蓄龄（日龄）
     */
    @Excel(name = "畜龄", orderNum = "1", width = 30)
    private Integer age;

    /**
     * 图像文件
     */
    @Excel(name = "图像文件", orderNum = "1", width = 60)
    private String imgFile;
}
