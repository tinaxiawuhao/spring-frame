package com.example.springframe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysPermission;
import com.example.springframe.mapper.SysPermissionMapper;
import com.example.springframe.service.SysPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限详情(SysPermission)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 11:03:47
 */
@Service("sysPermissionService")
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Resource
    private SysPermissionMapper sysPermissionMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysPermission queryById(Integer id) {
        return this.sysPermissionMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysPermission 筛选条件
     * @param pageRequest   分页对象
     * @return 查询结果
     */
    @Override
    public PageInfo<SysPermission> queryByPage(SysPermission sysPermission, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysPermission> permissionList = this.sysPermissionMapper.queryAllByLimit(sysPermission);
        return new PageInfo<>(permissionList);
    }

    /**
     * 新增数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysPermission insert(SysPermission sysPermission) {
        this.sysPermissionMapper.insert(sysPermission);
        return sysPermission;
    }

    /**
     * 修改数据
     *
     * @param sysPermission 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysPermission update(SysPermission sysPermission) {
        this.sysPermissionMapper.update(sysPermission);
        return this.queryById(sysPermission.getId());
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
        return this.sysPermissionMapper.deleteById(id) > 0;
    }
}
