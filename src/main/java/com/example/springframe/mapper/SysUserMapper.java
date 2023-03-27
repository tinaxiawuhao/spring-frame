package com.example.springframe.mapper;

import com.example.springframe.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户详情(SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 10:21:47
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询指定行数据
     *
     * @param sysUser 查询条件
     * @return 对象列表
     */
    List<SysUser> queryAllByLimit(SysUser sysUser);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysUser> entities);

    /**
     * 批量按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysUser> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int updateBatch(@Param("entities") List<SysUser> entities);

}
