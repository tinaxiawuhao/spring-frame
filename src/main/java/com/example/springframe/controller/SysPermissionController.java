package com.example.springframe.controller;

import com.example.springframe.entity.SysPermission;
import com.example.springframe.entity.to.SysPermissionTO;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 权限详情(SysPermission)表控制层
 *
 * @author makejava
 * @since 2023-03-27 10:21:44
 */
@Api(tags = "权限详情")
@RestController
@RequestMapping("sysPermission")
public class SysPermissionController {
    /**
     * 服务对象
     */
    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 分页查询
     *
     * @param sysPermission 筛选条件
     * @return 查询结果
     */
    @PostMapping(value = "/queryByPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public APIResponse<PageInfo<SysPermission>> queryByPage(@RequestBody SysPermissionTO sysPermission) {
        return APIResponse.ok(this.sysPermissionService.queryByPage(sysPermission));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping(value = "/queryById/{id}")
    @ApiOperation(value = "根据id获取单条数据", notes = "根据id获取单条数据")
    public APIResponse<SysPermission> queryById(@PathVariable("id") Integer id) {
        return APIResponse.ok(this.sysPermissionService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param sysPermission 实体
     * @return 新增结果
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增数据", notes = "新增数据")
    public APIResponse<SysPermission> add(@RequestBody @Valid SysPermission sysPermission) {
        return APIResponse.ok(this.sysPermissionService.insert(sysPermission));
    }

    /**
     * 编辑数据
     *
     * @param sysPermission 实体
     * @return 编辑结果
     */
    @PutMapping(value = "/edit")
    @ApiOperation(value = "编辑数据", notes = "编辑数据")
    public APIResponse<SysPermission> edit(@RequestBody @Valid SysPermission sysPermission) {
        return APIResponse.ok(this.sysPermissionService.update(sysPermission));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping(value = "/deleteById")
    @ApiOperation(value = "根据主键删除数据", notes = "根据主键删除数据")
    public APIResponse<Boolean> deleteById(Integer id) {
        return APIResponse.ok(this.sysPermissionService.deleteById(id));
    }

}

