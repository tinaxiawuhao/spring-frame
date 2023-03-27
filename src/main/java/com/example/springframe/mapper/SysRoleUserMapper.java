package com.example.springframe.mapper;

import com.example.springframe.entity.SysRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关系(SysRoleUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 10:21:46
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    /**
     * 查询指定行数据
     *
     * @param sysRoleUser 查询条件
     * @return 对象列表
     */
    List<SysRoleUser> queryAllByLimit(SysRoleUser sysRoleUser);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysRoleUser> entities);

    /**
     * 批量按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleUser> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int updateBatch(@Param("entities") List<SysRoleUser> entities);

}
