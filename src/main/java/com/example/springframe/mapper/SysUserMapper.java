package com.example.springframe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springframe.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
