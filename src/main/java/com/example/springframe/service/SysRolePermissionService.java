package com.example.springframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysRolePermission;
import com.github.pagehelper.PageInfo;

/**
 * (SysRolePermission)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 11:03:49
 */
public interface SysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRolePermission queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysRolePermission 筛选条件
     * @param pageRequest       分页对象
     * @return 查询结果
     */
    PageInfo<SysRolePermission> queryByPage(SysRolePermission sysRolePermission, SearchPage pageRequest);

    /**
     * 新增数据
     *
     * @param sysRolePermission 实例对象
     * @return 实例对象
     */
    SysRolePermission insert(SysRolePermission sysRolePermission);

    /**
     * 修改数据
     *
     * @param sysRolePermission 实例对象
     * @return 实例对象
     */
    SysRolePermission update(SysRolePermission sysRolePermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
