package com.example.springframe.service;

import com.example.springframe.entity.SysPermission;
import com.example.springframe.entity.to.SysPermissionTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * 权限详情(SysPermission)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysPermission queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysPermission 筛选条件
     * @return 查询结果
     */
    PageInfo<SysPermission> queryByPage(SysPermissionTO sysPermission);

    /**
     * 新增数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    SysPermission insert(SysPermission sysPermission);

    /**
     * 修改数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    SysPermission update(SysPermission sysPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
