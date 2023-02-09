package com.example.springframe.service;

import com.example.springframe.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.example.springframe.entity.SearchPage;

/**
 * (SysRole)表服务接口
 *
 * @author makejava
 * @since 2023-02-09 09:41:03
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
     * @param sysRole     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    PageInfo<SysRole> queryByPage(SysRole sysRole, SearchPage pageRequest);

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
