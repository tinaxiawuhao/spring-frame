package com.example.springframe.service.impl;

import com.example.springframe.entity.SysUser;
import com.example.springframe.mapper.SysUserMapper;
import com.example.springframe.service.SysUserService;
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
 * 用户详情(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 09:58:17
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryById(Integer id) {
        return this.sysUserMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param sysUser     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public PageInfo<SysUser> queryByPage(SysUser sysUser, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysUser> list = this.sysUserMapper.queryAllByLimit(sysUser);
        return new PageInfo<>(list);
    }

    /**
     * 新增数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysUser insert(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
        return sysUser;
    }

    /**
     * 修改数据
     *
     * @param sysUser 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public SysUser update(SysUser sysUser) {
        this.sysUserMapper.update(sysUser, Wrappers.<SysUser>lambdaUpdate().eq(SysUser::getId, sysUser.getId()));
        return this.queryById(sysUser.getId());
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
        return this.sysUserMapper.deleteById(id) > 0;
    }
}
