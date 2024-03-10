package com.xbp.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xbp.model.system.SysUser;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

}
