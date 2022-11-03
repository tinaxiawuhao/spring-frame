package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.mapper.SysRolePermissionMapper;
import com.example.springframe.service.SysRolePermissionService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (SysRolePermission)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 11:03:49
 */
@Service("sysRolePermissionService")
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements SysRolePermissionService {
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysRolePermission queryById(Integer id) {
        return this.sysRolePermissionMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRolePermission 筛选条件
     * @param pageRequest       分页对象
     * @return 查询结果
     */
    @Override
    public Page<SysRolePermission> queryByPage(SysRolePermission sysRolePermission, PageRequest pageRequest) {
        long total = this.sysRolePermissionMapper.count(sysRolePermission);
        return new PageImpl<>(this.sysRolePermissionMapper.queryAllByLimit(sysRolePermission, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param sysRolePermission 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRolePermission insert(SysRolePermission sysRolePermission) {
        this.sysRolePermissionMapper.insert(sysRolePermission);
        return sysRolePermission;
    }

    /**
     * 修改数据
     *
     * @param sysRolePermission 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRolePermission update(SysRolePermission sysRolePermission) {
        this.sysRolePermissionMapper.update(sysRolePermission);
        return this.queryById(sysRolePermission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean deleteById(Integer id) {
        return this.sysRolePermissionMapper.deleteById(id) > 0;
    }
}
