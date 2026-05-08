package org.example.wms_backend.service.impl;

import org.example.wms_backend.common.StringUtil;
import org.example.wms_backend.dto.UserDTO;
import org.example.wms_backend.dto.UserUpdateDTO;
import org.example.wms_backend.entity.User;
import org.example.wms_backend.mapper.UserMapper;
import org.example.wms_backend.service.UserService;
import org.example.wms_backend.utils.RedisUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户表服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(UserDTO userDTO) {
        // 检查账号是否已存在
        if (userMapper.selectByAccount(userDTO.getAccount()) != null) {
            return "注册失败：账号已存在";
        }

        // 将DTO转换为实体类
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 加密密码
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 设置默认值和时间
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDeleted(0);

        // 保存数据
        userMapper.insert(user);

        return "注册成功";
    }

    @Override
    public UserDTO login(String account, String password) {
        // 检查账号格式是否合法（8-12位纯数字）
        if (!account.matches("^\\d{8,12}$")) {
            return null;
        }

        // 检查账号是否存在
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            return null;
        }

        // 检查密码是否正确
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // 将实体类转换为DTO
        UserDTO userDTO = convertToDTO(user);
        // 清空密码，不返回给前端
        userDTO.setPassword(null);
        
        return userDTO;
    }

    @Override
    public UserDTO findByAccount(String account) {
        // 根据账号查询用户
        User user = userMapper.selectByAccount(account);
        
        // 将实体类转换为DTO
        if (user != null) {
            UserDTO userDTO = convertToDTO(user);
            // 清空密码，不返回给前端
            userDTO.setPassword(null);
            return userDTO;
        }
        return null;
    }

    @Override
    public List<UserDTO> findByName(String name) {
        // 根据姓名查询用户
        List<User> userList = userMapper.selectByName(name);
        
        // 将实体类转换为DTO
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                UserDTO userDTO = convertToDTO(user);
                // 清空密码，不返回给前端
                userDTO.setPassword(null);
                userDTOList.add(userDTO);
            }
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> findByAccountStatus(String accountStatus) {
        // 查询所有用户
        List<User> userList = userMapper.selectAll();
        
        // 根据账号状态筛选
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                // 根据isDeleted判断状态
                String status = (user.getIsDeleted() != null && user.getIsDeleted() == 1) ? "封禁" : "启用";
                if (status.equals(accountStatus)) {
                    UserDTO userDTO = convertToDTO(user);
                    // 清空密码，不返回给前端
                    userDTO.setPassword(null);
                    userDTOList.add(userDTO);
                }
            }
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> findByPermissionLevel(int permissionLevel) {
        // 根据权限等级查询用户
        List<User> userList = userMapper.selectByPermissionLevel(permissionLevel);
        
        // 将实体类转换为DTO
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                UserDTO userDTO = convertToDTO(user);
                // 清空密码，不返回给前端
                userDTO.setPassword(null);
                userDTOList.add(userDTO);
            }
        }
        return userDTOList;
    }

    @Override
    public Integer findPermissionLevelByAccount(String account) {
        // 根据账号查询权限等级
        return userMapper.selectPermissionLevelByAccount(account);
    }

    @Override
    public String updateIsDeleted(String account) {
        // 检查用户是否存在
        User existingUser = userMapper.selectByAccount(account);
        if (existingUser == null) {
            return "修改用户状态失败：用户不存在";
        }
        
        // 修改逻辑删除状态
        userMapper.updateIsDeletedByAccount(account);
        
        return "修改用户状态成功";
    }

    @Override
    public List<UserDTO> findAll() {
        // 查询所有用户
        List<User> userList = userMapper.selectAll();
        
        // 将实体类转换为DTO
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                UserDTO userDTO = convertToDTO(user);
                // 清空密码，不返回给前端
                userDTO.setPassword(null);
                userDTOList.add(userDTO);
            }
        }
        return userDTOList;
    }

    /**
     * 将User实体类转换为UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 根据isDeleted设置账号状态
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            userDTO.setAccountStatus("封禁");
        } else {
            userDTO.setAccountStatus("启用");
        }
        return userDTO;
    }

    /**
     * 将User实体类列表转换为UserDTO列表
     */
    private List<UserDTO> convertToDTOList(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                userDTOList.add(convertToDTO(user));
            }
        }
        return userDTOList;
    }

    @Override
    public String update(UserDTO userDTO) {
        // 检查用户是否存在
        User user = userMapper.selectByAccount(userDTO.getAccount());
        if (user == null) {
            return "修改失败：用户不存在";
        }
        
        // 只允许修改姓名和密码
        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKey(user);
        return "修改成功";
    }

    @Override
    public String update(UserUpdateDTO userUpdateDTO, String currentAccount) {
        // 验证提交的账号与当前登录账号是否一致
        if (userUpdateDTO.getAccount() != null && !userUpdateDTO.getAccount().equals(currentAccount)) {
            return "系统错误，只能修改自己的账号信息";
        }
        
        // 使用当前登录账号
        String account = currentAccount;
        
        // 从数据库获取用户信息
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            return "修改失败：用户不存在";
        }
        
        // 验证旧密码是否正确
        if (userUpdateDTO.getOldPassword() == null || userUpdateDTO.getOldPassword().isEmpty()) {
            return "修改失败：请输入旧密码";
        }
        
        if (!passwordEncoder.matches(userUpdateDTO.getOldPassword(), user.getPassword())) {
            return "修改失败：旧密码不正确";
        }
        
        // 验证新姓名格式（如果有修改）
        if (userUpdateDTO.getName() != null && !userUpdateDTO.getName().isEmpty()) {
            if (!userUpdateDTO.getName().matches("^[\\u4e00-\\u9fa5a-zA-Z]{2,10}$")) {
                return "修改失败：姓名只能是二到10位的大小写英文或者纯中文";
            }
            user.setName(userUpdateDTO.getName());
        }
        
        // 验证新密码格式（如果有修改）
        if (userUpdateDTO.getNewPassword() != null && !userUpdateDTO.getNewPassword().isEmpty()) {
            if (!userUpdateDTO.getNewPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$")) {
                return "修改失败：密码必须为8到16位大小写英文加数字加特殊字符的组合";
            }
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getNewPassword()));
        }
        
        // 执行更新
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKey(user);
        
        return "修改成功";
    }

    @Override
    public String updatePermission(String account, Integer permissionLevel) {
        // 检查用户是否存在
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            return "修改失败：用户不存在";
        }

        // 修改权限
        user.setPermissionLevel(permissionLevel);
        user.setUpdateTime(new Date());
        userMapper.updateByPrimaryKey(user);

        return "修改成功";
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<UserDTO> findByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> userList = userMapper.selectByPage(offset, pageSize);
        int total = userMapper.selectCount();
        // 转换为DTO并清空密码
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                UserDTO userDTO = convertToDTO(user);
                userDTO.setPassword(null);
                userDTOList.add(userDTO);
            }
        }
        return new org.example.wms_backend.dto.PageResponseDTO<>(userDTOList, total, page, pageSize);
    }

    @Override
    public org.example.wms_backend.dto.PageResponseDTO<UserDTO> findByNamePage(String name, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> userList = userMapper.selectByNamePage(name, offset, pageSize);
        int total = userMapper.selectCountByName(name);
        // 转换为DTO并清空密码
        List<UserDTO> userDTOList = new ArrayList<>();
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                UserDTO userDTO = convertToDTO(user);
                userDTO.setPassword(null);
                userDTOList.add(userDTO);
            }
        }
        return new org.example.wms_backend.dto.PageResponseDTO<>(userDTOList, total, page, pageSize);
    }
}