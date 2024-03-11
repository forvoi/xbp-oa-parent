package com.xbp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xbp.auth.service.SysMenuService;
import com.xbp.auth.service.SysUserService;
import com.xbp.model.system.SysUser;
import com.xbp.security.custom.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/11 10:05
 */
@Component
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }


        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String perm :userPermsList) {
            authorities.add(new SimpleGrantedAuthority(perm.trim()));
        }

        return new CustomUser(sysUser,authorities);
    }
}
