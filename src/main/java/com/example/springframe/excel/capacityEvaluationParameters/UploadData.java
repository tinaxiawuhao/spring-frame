package com.example.springframe.excel.capacityEvaluationParameters;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.springframe.excel.UploadDataInterface;
import lombok.Data;

/**
 * 基础数据类
 *
 **/
@Data
public class UploadData extends UploadDataInterface {

    /**
     * 产线id
     */
    @ExcelProperty(value = "产线/设备ID")
    private String productionLineId;
    /**
     * 产线名称
     */
    @ExcelProperty(value = "产线/设备名称")
    private String productionLineName;
    /**
     * 月份
     */
    @ExcelProperty(value = "月份")
    private String month;
    /**
     * 每月工作天数
     */
    @ExcelProperty(value = "每月工作天数")
    private Integer workingDayPerMonth;
    /**
     * 工作时长
     */
    @ExcelProperty(value = "每班次工作时长")
    private Float workingHours;
    /**
     * 每日班次
     */
    @ExcelProperty(value = "每天开班数")
    private Integer shiftsPerDay;
    /**
     * 新产品测试（小时）
     */
    @ExcelProperty(value = "新产品试制时长")
    private Integer newproductDebug;
    /**
     * 百分比%
     */
    @ExcelProperty(value = "百分比%")
    private Float oee;
    /**
     * 人员标准配置
     */
    @ExcelProperty(value = "人员标准配置")
    private Integer standardStaffing;
    /**
     * 产线类型(0:总装，1:装准，2:机加)
     */
    @ExcelProperty(value = "产线类型(0:总装，1:装准，2:机加)")
    private Integer productionLineType;


}
