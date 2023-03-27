package com.example.springframe.service.impl;

import com.example.springframe.entity.SysPermission;
import com.example.springframe.entity.to.SysPermissionTO;
import com.example.springframe.mapper.SysPermissionMapper;
import com.example.springframe.service.SysPermissionService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限详情(SysPermission)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
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
        return this.sysPermissionMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param sysPermission 筛选条件
     * @return 查询结果
     */
    @Override
    public PageInfo<SysPermission> queryByPage(SysPermissionTO sysPermission) {
        PageHelper.startPage(sysPermission.getPage(), sysPermission.getSize());
        SysPermission build = SysPermission.builder().build();
        BeanUtils.copyProperties(sysPermission, build);
        List<SysPermission> list = this.sysPermissionMapper.queryAllByLimit(build);
        return new PageInfo<>(list);
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
        this.sysPermissionMapper.update(sysPermission, Wrappers.<SysPermission>lambdaUpdate().eq(SysPermission::getId, sysPermission.getId()));
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
