package com.example.springframe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springframe.entity.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysRoleUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 11:03:49
 */
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRoleUser queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param sysRoleUser 查询条件
     * @return 对象列表
     */
    List<SysRoleUser> queryAllByLimit(SysRoleUser sysRoleUser);

    /**
     * 统计总行数
     *
     * @param sysRoleUser 查询条件
     * @return 总行数
     */
    long count(SysRoleUser sysRoleUser);

    /**
     * 新增数据
     *
     * @param sysRoleUser 实例对象
     * @return 影响行数
     */
    int insert(SysRoleUser sysRoleUser);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleUser> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysRoleUser> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysRoleUser> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysRoleUser> entities);

    /**
     * 修改数据
     *
     * @param sysRoleUser 实例对象
     * @return 影响行数
     */
    int update(SysRoleUser sysRoleUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

