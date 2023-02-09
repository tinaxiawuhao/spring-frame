package com.example.springframe.mapper;

import com.example.springframe.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 权限详情(SysPermission)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-09 09:44:52
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询指定行数据
     *
     * @param sysPermission 查询条件
     * @return 对象列表
     */
    List<SysPermission> queryAllByLimit(SysPermission sysPermission);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysPermission> entities);

    /**
     * 批量按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysPermission> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int updateBatch(@Param("entities") List<SysPermission> entities);

}
