package com.xbp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xbp.auth.mapper.SysRoleMapper;
import com.xbp.auth.service.SysRoleService;
import com.xbp.auth.service.SysUserRoleService;
import com.xbp.model.system.SysRole;
import com.xbp.model.system.SysUserRole;
import com.xbp.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 15:07
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleByAdminId(Long userId) {
        //查询所有角色
        List<SysRole> allRolesList = baseMapper.selectList(null);
        //拥有的角色ID
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

        List<Long> existRoleIdList = existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //对角色进行分类
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole role :allRolesList) {
            //已分配
            if (existRoleIdList.contains(role.getId())){
                assignRoleList.add(role);
            }
        }

       Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("allRolesList",allRolesList);
        return roleMap;
    }


    @Transactional
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {

        //把用户之前的分配角色删除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新添加新的角色列表
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId :roleIdList) {
            if (StringUtils.isEmpty(roleId)){
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleService.save(sysUserRole);

        }
    }


}
