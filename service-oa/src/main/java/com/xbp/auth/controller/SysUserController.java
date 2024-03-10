package com.xbp.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xbp.auth.service.SysRoleService;
import com.xbp.auth.service.SysUserService;
import com.xbp.common.result.Result;
import com.xbp.common.utils.MD5;
import com.xbp.model.system.SysUser;
import com.xbp.vo.system.AssginRoleVo;
import com.xbp.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
@Api(tags = "用户管理模块")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;

    @Autowired
    private SysRoleService sysRoleService;

    //更改用户状态
    @ApiOperation("更新用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){
        service.updateStatus(id,status);
        return Result.ok();
    }

    //查询所有角色和当前用户的角色
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String, Object> roleMap=sysRoleService.findRoleByAdminId(userId);
        return Result.ok(roleMap);
    }


    //根据用户分配角色
    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit, SysUserQueryVo sysUserQueryVo){
        //创建page对象
        Page<SysUser> pageParam = new Page<>(page, limit);
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        if (!StringUtils.isEmpty(userName)){
            wrapper.like(SysUser::getUsername,userName);
        }
        //Like模糊查询
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //ge大于等于
        if (!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le(SysUser::getUpdateTime,createTimeEnd);
        }
        //le小于等于

        //调用mapper方法实现查询
        IPage<SysUser> pageModel= service.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        //使用md5对密码进行加密
        String password = user.getPassword();
        String passwordMD5 = MD5.encrypt(password);
        user.setPassword(passwordMD5);
        service.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }
}

