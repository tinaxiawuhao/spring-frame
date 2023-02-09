package com.example.springframe.service.impl;

import com.example.springframe.entity.SysRoleUser;
import com.example.springframe.mapper.SysRoleUserMapper;
import com.example.springframe.service.SysRoleUserService;
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
 * 用户角色关系(SysRoleUser)表服务实现类
 *
 * @author makejava
 * @since 2023-02-09 09:58:17
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
        return this.sysRoleUserMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param sysRoleUser 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public PageInfo<SysRoleUser> queryByPage(SysRoleUser sysRoleUser, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysRoleUser> list = this.sysRoleUserMapper.queryAllByLimit(sysRoleUser);
        return new PageInfo<>(list);
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
        this.sysRoleUserMapper.update(sysRoleUser, Wrappers.<SysRoleUser>lambdaUpdate().eq(SysRoleUser::getId, sysRoleUser.getId()));
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
