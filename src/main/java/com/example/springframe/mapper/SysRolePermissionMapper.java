package com.example.springframe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springframe.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysRolePermission)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 11:03:49
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRolePermission queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param sysRolePermission 查询条件
     * @return 对象列表
     */
    List<SysRolePermission> queryAllByLimit(SysRolePermission sysRolePermission);

    /**
     * 统计总行数
     *
     * @param sysRolePermission 查询条件
     * @return 总行数
     */
    long count(SysRolePermission sysRolePermission);

    /**
     * 新增数据
     *
     * @param sysRolePermission 实例对象
     * @return 影响行数
     */
    int insert(SysRolePermission sysRolePermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRolePermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysRolePermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRolePermission> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysRolePermission> entities);

    /**
     * 修改数据
     *
     * @param sysRolePermission 实例对象
     * @return 影响行数
     */
    int update(SysRolePermission sysRolePermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

