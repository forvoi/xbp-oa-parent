package com.xbp.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xbp.auth.mapper.SysRoleMapper;
import com.xbp.model.system.SysRole;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 11:29
 */
@SpringBootTest
public class TestUser {

    @Autowired
    private SysRoleMapper mapper;

    /**
     * 练习
     */
    @Test
    public void getAll(){
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }
    
    /**
     * 插入新数据
     */
    @Test
    public void insertRole(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("边佳旗");
        sysRole.setRoleCode("bjq");
        sysRole.setDescription("你是边佳旗");

        int rows = mapper.insert(sysRole);
        System.out.println(rows);
        System.out.println(sysRole.getId());

    }


    /**
     * 修改
     */
    @Test
    public void update(){
        //根据id查询
        SysRole sysRole = mapper.selectById(14);
        //修改数据
        sysRole.setRoleName("不是边佳旗");
        //调用相关的API进行插入
        int rows = mapper.updateById(sysRole);

        System.out.println(rows);
    }

    /**
     * 删除
     */
    @Test
    public void deleteId(){
        int rows = mapper.deleteById(14);
        System.out.println(rows);


    }


    /**
     * 批量删除
     */
    @Test
    public void testDeleteBatchIds(){
        int rows = mapper.deleteBatchIds(Arrays.asList(1,14));
        System.out.println(rows);


    }

    /**
     * 条件查询
     */
    @Test
    public void queryWrapper1(){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name","薛斌鹏");
        List<SysRole> list = mapper.selectList(wrapper);
        System.out.println(list);

    }

    /**
     * 条件查询2
     */
    @Test
    public void queryWrapper2(){
        //调用方法分装条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"薛斌鹏");
        List<SysRole> list = mapper.selectList(wrapper);
        System.out.println(list);

    }
}
