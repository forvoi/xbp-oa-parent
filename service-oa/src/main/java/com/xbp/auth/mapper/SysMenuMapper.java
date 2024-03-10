package com.xbp.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xbp.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findListByUserId(@Param("userId") Long userId);
}
