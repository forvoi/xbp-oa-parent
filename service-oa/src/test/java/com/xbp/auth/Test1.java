package com.xbp.auth;

import com.xbp.auth.service.SysRoleService;
import com.xbp.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 15:09
 */
@SpringBootTest
public class Test1 {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询
     */
    @Test
    public void getAll(){
        List<SysRole> list = sysRoleService.list();
        System.out.println(list);

    }
}
