package org.example.wms_backend.service.impl;

import org.example.wms_backend.entity.User;
import org.example.wms_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService实现类
 * 用于从数据库获取用户信息，供Spring Security进行认证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        // 从数据库中根据账号查询用户
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 检查是否已被逻辑删除
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            throw new UsernameNotFoundException("账号已被封禁");
        }

        // 构建UserDetails对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getAccount())
                .password(user.getPassword())
                .authorities(user.getPermissionLevel() == 1 ? "ROLE_ADMIN" : "ROLE_USER")
                .build();
    }
}
