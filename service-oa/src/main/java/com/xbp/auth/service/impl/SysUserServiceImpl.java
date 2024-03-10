package com.xbp.auth.service.impl;

import com.xbp.model.system.SysRole;
import com.xbp.model.system.SysUser;
import com.xbp.auth.mapper.SysUserMapper;
import com.xbp.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void updateStatus(Long id, Integer status) {
        //根据用户id查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态
        sysUser.setStatus(status);

        //调用方法进行修改
        baseMapper.updateById(sysUser);
    }


}
