package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRole;
import com.example.springframe.mapper.SysRoleMapper;
import com.example.springframe.service.SysRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (SysRole)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 11:03:48
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
        return this.sysRoleMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRole     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<SysRole> queryByPage(SysRole sysRole, PageRequest pageRequest) {
        long total = this.sysRoleMapper.count(sysRole);
        return new PageImpl<>(this.sysRoleMapper.queryAllByLimit(sysRole, pageRequest), pageRequest, total);
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
        this.sysRoleMapper.update(sysRole);
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
