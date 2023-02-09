package com.example.springframe.service;

import com.example.springframe.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.example.springframe.entity.SearchPage;

/**
 * 用户详情(SysUser)表服务接口
 *
 * @author makejava
 * @since 2023-02-09 09:41:04
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysUser queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysUser     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    PageInfo<SysUser> queryByPage(SysUser sysUser, SearchPage pageRequest);

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser insert(SysUser sysUser);

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    SysUser update(SysUser sysUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
