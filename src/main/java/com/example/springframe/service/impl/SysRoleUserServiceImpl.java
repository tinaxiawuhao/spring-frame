package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRoleUser;
import com.example.springframe.mapper.SysRoleUserMapper;
import com.example.springframe.service.SysRoleUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (SysRoleUser)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 11:03:50
 */
@Service("sysRoleUserService")
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysRoleUser queryById(Integer id) {
        return this.sysRoleUserMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRoleUser 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<SysRoleUser> queryByPage(SysRoleUser sysRoleUser, PageRequest pageRequest) {
        long total = this.sysRoleUserMapper.count(sysRoleUser);
        return new PageImpl<>(this.sysRoleUserMapper.queryAllByLimit(sysRoleUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param sysRoleUser 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRoleUser insert(SysRoleUser sysRoleUser) {
        this.sysRoleUserMapper.insert(sysRoleUser);
        return sysRoleUser;
    }

    /**
     * 修改数据
     *
     * @param sysRoleUser 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysRoleUser update(SysRoleUser sysRoleUser) {
        this.sysRoleUserMapper.update(sysRoleUser);
        return this.queryById(sysRoleUser.getId());
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
        return this.sysRoleUserMapper.deleteById(id) > 0;
    }
}
