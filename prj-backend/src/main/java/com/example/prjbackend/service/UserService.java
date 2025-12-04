package com.example.prjbackend.service;

import com.example.prjbackend.domain.User;
import com.example.prjbackend.framework.TokenService;
import com.example.prjbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TokenService tokenService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public int register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            // 用户名已存在
            return -1;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        return userMapper.insert(user);
    }

    public String login(String username, String rawPassword) {
        User dbUser = userMapper.findByUsername(username);
        if (dbUser == null || dbUser.getActive() == null || dbUser.getActive() == 0) {
            return null;
        }
        if (!passwordEncoder.matches(rawPassword, dbUser.getPassword())) {
            return null;
        }
        return tokenService.createToken(dbUser.getId());
    }
}
