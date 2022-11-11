package com.example.springframe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysUser;
import com.example.springframe.entity.vo.RigistUserVO;
import com.example.springframe.exception.BusinessException;
import com.example.springframe.exception.basic.ResponseCode;
import com.example.springframe.mapper.SysUserMapper;
import com.example.springframe.service.SysUserService;
import com.example.springframe.utils.CurrentUserInfo;
import com.example.springframe.utils.redis.RedisContact;
import com.example.springframe.utils.redis.RedisUtil;
import com.example.springframe.utils.util.Bool;
import com.example.springframe.utils.util.Dates;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户详情(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 11:03:51
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private CurrentUserInfo currentUserInfo;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SysUser queryById(Integer id) {
        return this.sysUserMapper.queryById(id);
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
        List<SysUser> userList = this.sysUserMapper.queryAllByLimit(sysUser);
        return new PageInfo<>(userList);
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
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
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
        this.sysUserMapper.update(sysUser);
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

    @Override
    public SysUser register(RigistUserVO addedUser) {
        //判断唯一
        Bool.of(Objects.isNull(sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, addedUser.getUsername()))))
                .hasFalse(() -> {
                    throw new BusinessException(ResponseCode.ACCOUNT_EXIST);
                });

        SysUser build = SysUser.builder().build();
        //判断验证码
        Bool.of(Optional.of(Objects.nonNull(redisUtil.get(RedisContact.TX_COUNT_MESSAGE_EXPIRE + addedUser.getUsername()))))
                .hasTrue((v) -> Bool.of(Objects.equals(v,addedUser.getMessageCode()))
                        .hasFalse(() -> {
                            throw new BusinessException(ResponseCode.CODE_ERROR);
                        }).hasTrue(()->{
                            BeanUtils.copyProperties(addedUser,build);
                            build.setCreateTime(Dates.now().date());
                            build.setCreateBy(currentUserInfo.getSysUser().getCode());
                            insert(build);
                        })
                )
                .hasFalse(() -> {
            throw new BusinessException(ResponseCode.CODE_NOT_SEND);
        });

        return build;
    }
}
