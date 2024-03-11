package com.xbp.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xbp.auth.service.SysRoleService;
import com.xbp.common.config.exception.XbpException;
import com.xbp.common.result.Result;
import com.xbp.model.system.SysRole;
import com.xbp.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 */
@Api(tags = "角色管理模块")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){
        try{
//            int a=10/0;
        }catch (Exception e){
            //抛出自定义异常
            throw new XbpException(20001,"抛出自定义异常");
        }

        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit, SysRoleQueryVo sysRoleQueryVo){
        //调用Service中的方法实现
        //1.创建Page对象，传递分页参数
        Page<SysRole> pageParam = new Page<>(page, limit);

        //2.封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)){
            //封装like模糊查询
            wrapper.like(SysRole::getRoleName,roleName);
        }

        //调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据Id查询")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable Long id){
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){
        boolean is_success = sysRoleService.save(sysRole);
        if (is_success){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole){
        boolean is_success = sysRoleService.updateById(sysRole);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }


    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据ID删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean is_success = sysRoleService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }


    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}
