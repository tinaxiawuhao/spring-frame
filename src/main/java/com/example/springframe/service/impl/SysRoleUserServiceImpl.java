package com.example.springframe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysRoleUser;
import com.example.springframe.mapper.SysRoleUserMapper;
import com.example.springframe.service.SysRoleUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    public PageInfo<SysRoleUser> queryByPage(SysRoleUser sysRoleUser, SearchPage pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        List<SysRoleUser> roleUserList = this.sysRoleUserMapper.queryAllByLimit(sysRoleUser);
        return new PageInfo<>(roleUserList);
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
