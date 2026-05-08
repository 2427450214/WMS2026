package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.dto.UserDTO;
import org.example.wms_backend.dto.UserUpdateDTO;
import org.example.wms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(
            @RequestParam String account,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam(required = false) String inviteCode) {
        
        UserDTO userDTO = new UserDTO();
        userDTO.setAccount(account);
        userDTO.setName(name);
        userDTO.setPassword(password);
        userDTO.setInviteCode(inviteCode);
        
        // 邀请码处理
        if ("RootShengWms".equals(inviteCode)) {
            userDTO.setPermissionLevel(1);
        } else {
            userDTO.setPermissionLevel(0);
        }

        // 设置默认账号状态
        userDTO.setAccountStatus("启用");

        // 调用服务层注册
        String result = userService.register(userDTO);
        return ResponseResult.success(result);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String account, @RequestParam String password) {
        // 此方法将由Spring Security的formLogin处理
        // 这里可以添加额外的登录逻辑，如登录日志记录等
        return ResponseResult.success("登录成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Map<String, Object> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseResult.error("未登录，请先登录");
        }
        String currentAccount = authentication.getName();
        UserDTO userDTO = userService.findByAccount(currentAccount);
        if (userDTO != null) {
            return ResponseResult.success(userDTO);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 根据账号查询用户（普通用户只能查看自己，管理员可以查看所有）
     */
    @GetMapping("/findByAccount")
    public Map<String, Object> findByAccount(@RequestParam String account) {
        if (account == null || account.trim().isEmpty()) {
            return ResponseResult.error("账号不能为空");
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseResult.error("未登录，请先登录");
        }
        String currentAccount = authentication.getName();
        
        // 检查是否为管理员
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        
        // 普通用户只能查看自己
        if (!isAdmin && !currentAccount.equals(account)) {
            return ResponseResult.error("没有权限查看其他用户信息");
        }
        
        UserDTO userDTO = userService.findByAccount(account);
        if (userDTO != null) {
            return ResponseResult.success(userDTO);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 根据姓名查询用户（需要管理员权限）
     */
    @GetMapping("/findByName")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseResult.error("姓名不能为空");
        }
        List<UserDTO> userDTOList = userService.findByName(name);
        if (userDTOList != null && !userDTOList.isEmpty()) {
            return ResponseResult.success(userDTOList);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 根据账号状态查询用户（需要管理员权限）
     */
    @GetMapping("/findByStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findByAccountStatus(@RequestParam String accountStatus) {
        if (accountStatus == null || accountStatus.trim().isEmpty()) {
            return ResponseResult.error("账号状态不能为空");
        }
        List<UserDTO> userDTOList = userService.findByAccountStatus(accountStatus);
        if (userDTOList != null && !userDTOList.isEmpty()) {
            return ResponseResult.success(userDTOList);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 根据权限等级查询用户（需要管理员权限）
     */
    @GetMapping("/findByPermissionLevel")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findByPermissionLevel(@RequestParam(required = false) Integer permissionLevel) {
        if (permissionLevel == null) {
            return ResponseResult.error("权限等级不能为空");
        }
        List<UserDTO> userDTOList = userService.findByPermissionLevel(permissionLevel);
        if (userDTOList != null && !userDTOList.isEmpty()) {
            return ResponseResult.success(userDTOList);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 查询所有用户（需要管理员权限）
     */
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> findAll() {
        List<UserDTO> userDTOList = userService.findAll();
        if (userDTOList != null && !userDTOList.isEmpty()) {
            return ResponseResult.success(userDTOList);
        } else {
            return ResponseResult.error("未找到用户");
        }
    }

    /**
     * 修改用户状态（需要管理员权限）
     */
    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updateStatus(@RequestParam String account) {
        if (account == null || account.trim().isEmpty()) {
            return ResponseResult.error("账号不能为空");
        }
        String result = userService.updateIsDeleted(account);
        return ResponseResult.success(result);
    }

    /**
     * 更新用户信息（只能修改自己的姓名和密码）
     */
    @PutMapping("/update")
    public Map<String, Object> update(@Validated UserUpdateDTO userUpdateDTO) {
        // 获取当前登录用户的账号
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseResult.error("未登录，请先登录");
        }
        String currentAccount = authentication.getName();
        
        String result = userService.update(userUpdateDTO, currentAccount);
        if (result.startsWith("修改成功")) {
            return ResponseResult.success(result);
        } else {
            return ResponseResult.error(result);
        }
    }

    /**
     * 修改用户权限（需要管理员权限和最高权限码）
     */
    @PutMapping("/updatePermission")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> updatePermission(
            @RequestParam String account,
            @RequestParam Integer permissionLevel,
            @RequestParam String inviteCode) {
        if (account == null || account.trim().isEmpty()) {
            return ResponseResult.error("账号不能为空");
        }
        if (permissionLevel == null || (permissionLevel != 0 && permissionLevel != 1)) {
            return ResponseResult.error("权限等级不正确");
        }
        // 验证最高权限码
        if (!"RootShengWms".equals(inviteCode)) {
            return ResponseResult.error("最高权限码不正确");
        }
        String result = userService.updatePermission(account, permissionLevel);
        if (result.startsWith("修改成功")) {
            return ResponseResult.success(result);
        } else {
            return ResponseResult.error(result);
        }
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return ResponseResult.success("退出登录成功");
    }

    /**
     * 分页查询用户（需要管理员权限）
     */
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getUserByPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<UserDTO> pageResponse = userService.findByPage(page, pageSize);
        return ResponseResult.success(pageResponse);
    }

    /**
     * 按姓名模糊查询分页（需要管理员权限）
     */
    @GetMapping("/searchPage")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> searchUserByPage(@RequestParam String name, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        org.example.wms_backend.dto.PageResponseDTO<UserDTO> pageResponse = userService.findByNamePage(name, page, pageSize);
        return ResponseResult.success(pageResponse);
    }
}