package com.xbp.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xbp.model.system.SysRole;
import com.xbp.vo.system.AssginRoleVo;

import java.util.Map;
import java.util.Objects;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 15:06
 */
public interface SysRoleService extends IService<SysRole> {
    Map<String, Object> findRoleByAdminId(Long userId);


    void doAssign(AssginRoleVo assginRoleVo);


}
