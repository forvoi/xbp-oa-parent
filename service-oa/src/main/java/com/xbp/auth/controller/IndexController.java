package com.xbp.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xbp.auth.service.SysMenuService;
import com.xbp.auth.service.SysUserService;
import com.xbp.common.config.exception.XbpException;
import com.xbp.common.jwt.JwtHelper;
import com.xbp.common.result.Result;
import com.xbp.common.utils.MD5;
import com.xbp.model.system.SysUser;
import com.xbp.vo.system.LoginVo;
import com.xbp.vo.system.RouterVo;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/5 11:00
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    //login
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){


//        HashMap<String, Object> map = new HashMap<>();
//        map.put("token","admin");
//        return Result.ok(map);
        //获取输入的用户名和密码
        //根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);

        //用户信息是否存在
        if (sysUser==null){
            throw new XbpException(201,"该用户不存在");
        }
        //判断密码
        //数据库密码MD5
        String password_db = sysUser.getPassword();
        //获取输入的密码
        String password_input = MD5.encrypt(loginVo.getPassword());
        if (!password_db.equals(password_input)){
            throw new XbpException(201,"密码错误");
        }
        //判断用户是否被禁用 1可用 0禁用
        if (sysUser.getStatus()==0){
            throw new XbpException(201,"当前用户不可用");
        }
        //使用jwt根据用户名和用户id生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        HashMap<String, Object> mapToken = new HashMap<>();
        mapToken.put("token", token);
        return Result.ok(mapToken);

    }

    //info
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        //1.从请求头获取Token信息（获取请求头Token字符串）
        String token = request.getHeader("token");
        //2.从Token中获取用户ID和用户名称
        Long userId = JwtHelper.getUserId(token);
//        String username = JwtHelper.getUsername(token);
        //3.根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);
        //4.根据用户id获取用户可以操作菜单列表
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerList= sysMenuService.findSysMenuListByUserId(userId);
        //5.根据用户id获取用户可以操作按钮列表
        List<String> permsList= sysMenuService.findUserPermsByUserId(userId);

        //6.返回相应的数据
        map.put("roles","[admin]");
        map.put("name",sysUser.getUsername());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作菜单
        map.put("routers",routerList);
        //返回用户可以操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    //logout
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
