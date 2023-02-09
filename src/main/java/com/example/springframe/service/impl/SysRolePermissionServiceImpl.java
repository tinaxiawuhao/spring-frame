package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.mapper.SysRolePermissionMapper;
import com.example.springframe.service.SysRolePermissionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;
import com.example.springframe.entity.SearchPage;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色权限关系(SysRolePermission)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 09:58:17
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
        return this.sysRolePermissionMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRolePermission 筛选条件
     * @param pageRequest       分页对象
     * @return 查询结果
     */
    @Override
    public PageInfo<SysRolePermission> queryByPage(SysRolePermission sysRolePermission, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysRolePermission> list = this.sysRolePermissionMapper.queryAllByLimit(sysRolePermission);
        return new PageInfo<>(list);
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
        this.sysRolePermissionMapper.update(sysRolePermission, Wrappers.<SysRolePermission>lambdaUpdate().eq(SysRolePermission::getId, sysRolePermission.getId()));
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
