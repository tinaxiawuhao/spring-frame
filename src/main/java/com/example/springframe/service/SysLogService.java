package com.example.springframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springframe.aop.log.SysLogBO;
import com.example.springframe.entity.SysLog;


/**
 * @author
 * @date 2022-11-12 14:39
 */
public interface SysLogService extends IService<SysLog> {
    void saveLog(SysLogBO sysLogBO);
}
