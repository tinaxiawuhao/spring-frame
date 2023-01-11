package com.example.springframe.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.aop.log.SysLogBO;
import com.example.springframe.aop.log.SysLogExtBO;
import com.example.springframe.entity.SysLog;
import com.example.springframe.entity.SysLogExt;
import com.example.springframe.mapper.SysLogMapper;
import com.example.springframe.service.SysLogExtService;
import com.example.springframe.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author
 * @date 2022-11-12 14:40
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Autowired
    private SysLogExtService logExtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(SysLogBO sysLogBO) {

        SysLog sysLog = SysLog.builder().build();
        BeanUtil.copyProperties(sysLogBO, sysLog);
        save(sysLog);
        SysLogExtBO logExt = sysLogBO.getLogExt();
        if (Objects.nonNull(logExt)) {
            SysLogExt sysLogExt = SysLogExt.builder().build();
            BeanUtil.copyProperties(logExt, sysLogExt);
            sysLogExt.setLogId(sysLog.getId());
            sysLogExt.setCreateTime(sysLog.getCreateTime());
            sysLogExt.setCreateBy(sysLog.getCreateBy());
            sysLogExt.setCreateById(sysLog.getCreateById());
            sysLogExt.setUpdateTime(sysLog.getUpdateTime());
            sysLogExt.setUpdateBy(sysLog.getUpdateBy());
            sysLogExt.setUpdateById(sysLog.getUpdateById());
            logExtService.save(sysLogExt);
        }

    }
}
