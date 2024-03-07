package com.xbp.auth.utils;

import com.xbp.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/6 15:57
 */
public class MenuHelper {
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //遍历寻找头数据
        ArrayList<SysMenu> trees = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId().longValue()==0){
                trees.add(findChildren(sysMenu,sysMenuList));
            }
        }

        return trees;
    }


    /**
     * @param sysMenu
     * @Description: 递归查找子节点
     * @param sysMenuList
     * @return
     */
    public static SysMenu findChildren(SysMenu sysMenu,List<SysMenu> sysMenuList){
        sysMenu.setChildren(new ArrayList<>());

        for (SysMenu it : sysMenuList) {
            if (it.getParentId().longValue()==sysMenu.getId().longValue()){
                if (sysMenu.getChildren()==null){
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(it,sysMenuList));
            }
        }
        return sysMenu;
    }
}
