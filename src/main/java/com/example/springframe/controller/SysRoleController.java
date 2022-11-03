package com.example.springframe.controller;

import com.example.springframe.entity.SearchPage;
import com.example.springframe.entity.SysRole;
import com.example.springframe.rest.RestResult;
import com.example.springframe.service.SysRoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysRole)表控制层
 *
 * @author makejava
 * @since 2022-11-03 11:03:47
 */
@Api(tags = "角色详情")
@RestController
@RequestMapping("sysRole")
public class SysRoleController {
    /**
     * 服务对象
     */
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 分页查询
     *
     * @param sysRole     筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @GetMapping(value = "/queryByPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public RestResult<PageInfo<SysRole>> queryByPage(@RequestBody SysRole sysRole, SearchPage pageRequest) {
        return RestResult.ok(this.sysRoleService.queryByPage(sysRole, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping(value = "/queryById/{id}")
    @ApiOperation(value = "根据id获取单条数据", notes = "根据id获取单条数据")
    public RestResult<SysRole> queryById(@PathVariable("id") Integer id) {
        return RestResult.ok(this.sysRoleService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRole 实体
     * @return 新增结果
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增数据", notes = "新增数据")
    public RestResult<SysRole> add(@RequestBody SysRole sysRole) {
        return RestResult.ok(this.sysRoleService.insert(sysRole));
    }

    /**
     * 编辑数据
     *
     * @param sysRole 实体
     * @return 编辑结果
     */
    @PutMapping(value = "/edit")
    @ApiOperation(value = "编辑数据", notes = "编辑数据")
    public RestResult<SysRole> edit(@RequestBody SysRole sysRole) {
        return RestResult.ok(this.sysRoleService.update(sysRole));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "根据主键删除数据", notes = "根据主键删除数据")
    public RestResult<Boolean> deleteById(Integer id) {
        return RestResult.ok(this.sysRoleService.deleteById(id));
    }

}

