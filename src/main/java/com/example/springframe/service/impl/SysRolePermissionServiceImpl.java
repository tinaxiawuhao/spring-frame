package com.example.springframe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.mapper.SysRolePermissionMapper;
import com.example.springframe.service.SysRolePermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    public PageInfo<SysRolePermission> queryByPage(SysRolePermission sysRolePermission, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysRolePermission> rolePermissionList = this.sysRolePermissionMapper.queryAllByLimit(sysRolePermission);
        return new PageInfo<>(rolePermissionList);
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
