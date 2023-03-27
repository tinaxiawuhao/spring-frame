package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRole;
import com.example.springframe.entity.to.SysRoleTO;
import com.example.springframe.mapper.SysRoleMapper;
import com.example.springframe.service.SysRoleService;
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
 * 角色详情(SysRole)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 10:21:45
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysRole queryById(Integer id) {
        return this.sysRoleMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRole 筛选条件
     * @return 查询结果
     */
    @Override
    public PageInfo<SysRole> queryByPage(SysRoleTO sysRole) {
        PageHelper.startPage(sysRole.getPage(), sysRole.getSize());
        SysRole build = SysRole.builder().build();
        BeanUtils.copyProperties(sysRole, build);
        List<SysRole> list = this.sysRoleMapper.queryAllByLimit(build);
        return new PageInfo<>(list);
    }

    /**
     * 新增数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRole insert(SysRole sysRole) {
        this.sysRoleMapper.insert(sysRole);
        return sysRole;
    }

    /**
     * 修改数据
     *
     * @param sysRole 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRole update(SysRole sysRole) {
        this.sysRoleMapper.update(sysRole, Wrappers.<SysRole>lambdaUpdate().eq(SysRole::getId, sysRole.getId()));
        return this.queryById(sysRole.getId());
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
        return this.sysRoleMapper.deleteById(id) > 0;
    }
}
