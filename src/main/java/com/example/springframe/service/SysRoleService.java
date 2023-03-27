package com.example.springframe.service;

import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.to.SysRoleTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * 角色详情(SysRole)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRole queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysRole 筛选条件
     * @return 查询结果
     */
    PageInfo<SysRole> queryByPage(SysRoleTO sysRole);

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole insert(SysRole sysRole);

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    SysRole update(SysRole sysRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
