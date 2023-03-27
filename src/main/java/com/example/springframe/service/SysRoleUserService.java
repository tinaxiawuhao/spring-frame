package com.example.springframe.service;

import com.example.springframe.entity.SysRoleUser;
import com.example.springframe.entity.to.SysRoleUserTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
 * 用户角色关系(SysRoleUser)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 10:21:46
 */
public interface SysRoleUserService extends IService<SysRoleUser> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SysRoleUser queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysRoleUser 筛选条件
     * @return 查询结果
     */
    PageInfo<SysRoleUser> queryByPage(SysRoleUserTO sysRoleUser);

    /**
     * 新增数据
     *
     * @param sysRoleUser 实例对象
     * @return 实例对象
     */
    SysRoleUser insert(SysRoleUser sysRoleUser);

    /**
     * 修改数据
     *
     * @param sysRoleUser 实例对象
     * @return 实例对象
     */
    SysRoleUser update(SysRoleUser sysRoleUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
