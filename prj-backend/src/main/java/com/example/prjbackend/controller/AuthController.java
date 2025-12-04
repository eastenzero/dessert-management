package com.example.prjbackend.controller;

import com.example.prjbackend.common.core.domain.AjaxResult;
import com.example.prjbackend.domain.User;
import com.example.prjbackend.framework.TokenService;
import com.example.prjbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody User user) {
        int rows = userService.register(user);
        if (rows == -1) {
            return AjaxResult.error(400, "用户名已存在");
        }
        return AjaxResult.success("注册成功");
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user) {
        String token = userService.login(user.getUsername(), user.getPassword());
        if (token == null) {
            return AjaxResult.error(400, "用户名或密码错误，或用户不可用");
        }
        return AjaxResult.success("登录成功", Map.of("token", token));
    }

    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenService.removeToken(token);
        }
        return AjaxResult.success("退出成功");
    }
}
