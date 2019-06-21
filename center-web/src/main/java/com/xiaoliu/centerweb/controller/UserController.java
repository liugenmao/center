package com.xiaoliu.centerweb.controller;

import com.xiaoliu.centerbiz.domain.User;
import com.xiaoliu.centerbiz.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        User user = userService.getUserByUsername("admin");
        model.addAttribute("user", user);
        return "index";
    }
}
