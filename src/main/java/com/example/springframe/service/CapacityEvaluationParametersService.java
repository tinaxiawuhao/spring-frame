package com.example.springframe.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springframe.entity.CapacityEvaluationParameters;
import com.example.springframe.entity.SearchPage;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 产能评估参数(CapacityEvaluationParameters)表服务接口
 *
 * @author makejava
 * @since 2023-01-05 16:18:54
 */
public interface CapacityEvaluationParametersService extends IService<CapacityEvaluationParameters> {

    /**
     * 新增数据
     *
     * @param capacityEvaluationParameters 实例对象
     * @return 实例对象
     */
    CapacityEvaluationParameters insert(CapacityEvaluationParameters capacityEvaluationParameters);

    /**
     * 获取产能评估参数列表
     * @param productionLineType
     * @return
     */
    List<CapacityEvaluationParameters> listByType(Integer productionLineType);

}
