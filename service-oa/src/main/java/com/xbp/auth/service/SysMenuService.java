package com.xbp.auth.service;

import com.xbp.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xbp.vo.system.AssignMenuVo;
import com.xbp.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);

    List<RouterVo> findSysMenuListByUserId(Long userId);

    List<String> findUserPermsByUserId(Long userId);
}
