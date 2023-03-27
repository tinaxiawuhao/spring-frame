package com.example.springframe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springframe.entity.SysLogExt;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2022-11-12 14:36
 */
@Mapper
public interface SysLogExtMapper extends BaseMapper<SysLogExt> {
}
