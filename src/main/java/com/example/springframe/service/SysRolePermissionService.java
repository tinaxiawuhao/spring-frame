package com.example.springframe.service;

import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.entity.to.SysRolePermissionTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * 角色权限关系(SysRolePermission)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 10:21:46
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
     * @return 查询结果
     */
    PageInfo<SysRolePermission> queryByPage(SysRolePermissionTO sysRolePermission);

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
