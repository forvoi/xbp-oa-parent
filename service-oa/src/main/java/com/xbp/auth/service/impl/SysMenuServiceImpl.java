package com.xbp.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xbp.auth.service.SysRoleMenuService;
import com.xbp.auth.utils.MenuHelper;
import com.xbp.common.config.exception.XbpException;
import com.xbp.model.system.SysMenu;
import com.xbp.auth.mapper.SysMenuMapper;
import com.xbp.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xbp.model.system.SysRoleMenu;
import com.xbp.vo.system.AssignMenuVo;
import com.xbp.vo.system.MetaVo;
import com.xbp.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;




/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xbp
 * @since 2024-03-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Override
    public List<SysMenu> findNodes() {
        //全部权限列表
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        if (StringUtils.isEmpty(sysMenuList)) return null;


        //构建树形数据
        List<SysMenu> result=MenuHelper.buildTree(sysMenuList);
        return result;
    }

    @Override
    public void removeMenuById(Long id) {
        //判断当前菜单是否有下一层菜单
        //如果有其他数据的parentId和当前id相等，说明当前id有下层id
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);
        Integer tag = baseMapper.selectCount(wrapper);
        if (tag>0){
            throw new XbpException(201,"菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //全部权限列表
        LambdaQueryWrapper<SysMenu> allSysMenuWrapper = new LambdaQueryWrapper<>();
        allSysMenuWrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> allSysMenuList = baseMapper.selectList(allSysMenuWrapper);
        //根据角色id获取角色权限
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuWrapper);
        //转换给角色id与角色权限对应Map对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());

        allSysMenuList.forEach(e->{
            if (menuIdList.contains(e.getId())){
                e.setSelect(true);
            }else {
                e.setSelect(false);
            }
        });

        List<SysMenu> sysMenuList = MenuHelper.buildTree(allSysMenuList);

        return sysMenuList;
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,assignMenuVo.getRoleId());

        sysRoleMenuService.remove(wrapper);

        for (Long menuId:assignMenuVo.getMenuIdList()){
            if (StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);

            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    /**
     * @param userId
     * @Description 查询数据库动态构建路由结构，进行显示
     * @return
     */
    @Override
    public List<RouterVo> findSysMenuListByUserId(Long userId) {
        //创建接收菜单
        List<SysMenu> sysMenuList=null;
        //1.判断当前用户是不是管理员 userid=1
        //1.1是管理员，查询所有
        //1.2不是管理员，根据userid查询可以操作的菜单
        if (userId.longValue()==1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            sysMenuList=baseMapper.findListByUserId(userId);
        }
        //2.把查询出来的数据列表构建成框架要求的路由结构
        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);
        List<RouterVo> routerVos = this.buileRouter(sysMenuTreeList);
        return routerVos;
    }

    private List<RouterVo> buileRouter(List<SysMenu> sysMenuTreeList) {
        //创建list集合存储最终数据
        ArrayList<RouterVo> routerVos = new ArrayList<>();
        //menu遍历
        for (SysMenu menu :sysMenuTreeList) {
            RouterVo routerVo = new RouterVo();
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setPath(getRouterPath(menu));
            routerVo.setComponent(menu.getComponent());
            routerVo.setMeta(new MetaVo(menu.getName(),menu.getIcon()));
            //下一层数据部分
            List<SysMenu> children = menu.getChildren();
            if (menu.getType()==1){
                //加载出下面的隐藏路由
                List<SysMenu> hiddenMenuList = children.stream().filter(item->!StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routerVos.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)) {
                    if(children.size() > 0) {
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buileRouter(children));
                }
            }
            routerVos.add(routerVo);
        }
        return routerVos;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    /**
     * @param userId
     * @Description 根据用户id获取可以操作的额按钮列表
     * @return
     */
    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        //3.从查询出来的数据里面，获取可以操作的按钮值的List集合
        List<SysMenu> sysMenuList=null;

        if (userId.longValue()==1){
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
//            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            sysMenuList=baseMapper.findListByUserId(userId);
        }

        List<String> permsList = sysMenuList.stream().filter(item -> item.getType() == 2).map(item -> item.getPerms()).collect(Collectors.toList());
        return permsList;
    }
}
