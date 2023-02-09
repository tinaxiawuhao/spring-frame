package com.example.springframe.controller;

import com.example.springframe.entity.SysRolePermission;
import com.example.springframe.entity.SearchPage;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.service.SysRolePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * (SysRolePermission)表控制层
 *
 * @author makejava
 * @since 2023-02-09 09:41:41
 */
@Api(tags = "")
@RestController
@RequestMapping("sysRolePermission")
public class SysRolePermissionController {
    /**
     * 服务对象
     */
    @Resource
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 分页查询
     *
     * @param sysRolePermission 筛选条件
     * @param pageRequest       分页对象
     * @return 查询结果
     */
    @PostMapping(value = "/queryByPage")
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    public APIResponse<PageInfo<SysRolePermission>> queryByPage(@RequestBody SysRolePermission sysRolePermission, SearchPage pageRequest) {
        return APIResponse.ok(this.sysRolePermissionService.queryByPage(sysRolePermission, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping(value = "/queryById/{id}")
    @ApiOperation(value = "根据id获取单条数据", notes = "根据id获取单条数据")
    public APIResponse<SysRolePermission> queryById(@PathVariable("id") Integer id) {
        return APIResponse.ok(this.sysRolePermissionService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param sysRolePermission 实体
     * @return 新增结果
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "新增数据", notes = "新增数据")
    public APIResponse<SysRolePermission> add(@RequestBody @Valid SysRolePermission sysRolePermission) {
        return APIResponse.ok(this.sysRolePermissionService.insert(sysRolePermission));
    }

    /**
     * 编辑数据
     *
     * @param sysRolePermission 实体
     * @return 编辑结果
     */
    @PutMapping(value = "/edit")
    @ApiOperation(value = "编辑数据", notes = "编辑数据")
    public APIResponse<SysRolePermission> edit(@RequestBody @Valid SysRolePermission sysRolePermission) {
        return APIResponse.ok(this.sysRolePermissionService.update(sysRolePermission));
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
        return APIResponse.ok(this.sysRolePermissionService.deleteById(id));
    }

}

