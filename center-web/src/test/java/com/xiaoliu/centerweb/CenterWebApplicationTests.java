package com.xiaoliu.centerweb;

import com.xiaoliu.CenterWebApplication;
import com.xiaoliu.centerbiz.common.result.Result;
import com.xiaoliu.centerbiz.domain.User;
import com.xiaoliu.centerbiz.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CenterWebApplication.class)
public class CenterWebApplicationTests {

    @Resource
    private IUserService userService;

    @Test
    public void getUserByUsernameTest() {
        User user = userService.getUserByUsername("admin");
        System.out.println(user);
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        Result result = userService.updateUser(user);
        System.out.println(result.getMessage());
    }

}
