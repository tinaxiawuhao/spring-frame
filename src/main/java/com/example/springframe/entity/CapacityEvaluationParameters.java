package com.example.springframe.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 产能评估参数(CapacityEvaluationParameters)实体类
 *
 * @author makejava
 * @since 2023-02-09 11:06:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("capacity_evaluation_parameters")
@ApiModel(value = "CapacityEvaluationParameters对象", description = "产能评估参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapacityEvaluationParameters extends Model<CapacityEvaluationParameters> implements Serializable {
    private static final long serialVersionUID = 354239040165176482L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 产线名称
     */
    @ApiModelProperty(value = "产线名称", required = true)
    @NotNull(message = "产线名称不能为空")
    private String productionLineName;
    /**
     * 每月工作天数
     */
    @ApiModelProperty(value = "每月工作天数", required = true)
    @NotNull(message = "每月工作天数不能为空")
    private Integer workingDayPerMonth;
    /**
     * 工作时长
     */
    @ApiModelProperty(value = "工作时长", required = true)
    @NotNull(message = "工作时长不能为空")
    private Float workingHours;
    /**
     * 每日班次
     */
    @ApiModelProperty(value = "每日班次", required = true)
    @NotNull(message = "每日班次不能为空")
    private Integer shiftsPerDay;
    /**
     * 新产品测试（小时）
     */
    @ApiModelProperty(value = "新产品测试（小时）", required = true)
    @NotNull(message = "新产品测试（小时）不能为空")
    private Integer newproductDebug;
    /**
     * 百分比%
     */
    @ApiModelProperty(value = "百分比%", required = true)
    @NotNull(message = "百分比%不能为空")
    private Float oee;
    /**
     * 人员标准配置
     */
    @ApiModelProperty(value = "人员标准配置", required = true)
    @NotNull(message = "人员标准配置不能为空")
    private Integer standardStaffing;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    @NotNull(message = "备注不能为空")
    private String remarks;
    /**
     * 产线类型(0:总装，1:装准，2:机加)
     */
    @ApiModelProperty(value = "产线类型(0:总装，1:装准，2:机加)", required = true)
    @NotNull(message = "产线类型(0:总装，1:装准，2:机加)不能为空")
    private Integer productionLineType;
    /**
     * 产线id
     */
    @ApiModelProperty(value = "产线id", required = true)
    @NotNull(message = "产线id不能为空")
    private String productionLineId;
    /**
     * 月份
     */
    @ApiModelProperty(value = "月份", required = true)
    @NotNull(message = "月份不能为空")
    private String month;
}
