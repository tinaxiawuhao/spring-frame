package com.example.springframe.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.entity.CapacityEvaluationParameters;
import com.example.springframe.mapper.CapacityEvaluationParametersMapper;
import com.example.springframe.service.CapacityEvaluationParametersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 产能评估参数(CapacityEvaluationParameters)表服务实现类
 *
 * @author makejava
 * @since 2023-01-05 16:18:55
 */
@Service("capacityEvaluationParametersService")
public class CapacityEvaluationParametersServiceImpl extends ServiceImpl<CapacityEvaluationParametersMapper, CapacityEvaluationParameters> implements CapacityEvaluationParametersService {
    @Resource
    private CapacityEvaluationParametersMapper capacityEvaluationParametersMapper;


    @Override
    public List<CapacityEvaluationParameters> listByType(Integer productionLineType) {
        return capacityEvaluationParametersMapper.selectList(Wrappers.<CapacityEvaluationParameters>lambdaQuery().eq(CapacityEvaluationParameters::getProductionLineType, productionLineType));
    }

    /**
     * 新增数据
     *
     * @param capacityEvaluationParameters 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public CapacityEvaluationParameters insert(CapacityEvaluationParameters capacityEvaluationParameters) {
        this.capacityEvaluationParametersMapper.insert(capacityEvaluationParameters);
        return capacityEvaluationParameters;
    }

    @Override
    @Transactional
    public boolean saveBatch(Collection<CapacityEvaluationParameters> cachedDataList) {
        cachedDataList.forEach(this::insert);
        return true;
    }
}
