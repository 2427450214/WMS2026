package org.example.wms_backend.service;

import org.example.wms_backend.dto.UserDTO;
import org.example.wms_backend.dto.UserUpdateDTO;
import java.util.List;

public interface UserService {
    String register(UserDTO userDTO);
    UserDTO login(String account, String password);
    UserDTO findByAccount(String account);
    List<UserDTO> findByName(String name);
    List<UserDTO> findByAccountStatus(String accountStatus);
    List<UserDTO> findByPermissionLevel(int permissionLevel);
    Integer findPermissionLevelByAccount(String account);
    String updateIsDeleted(String account);
    List<UserDTO> findAll();
    String update(UserDTO userDTO);
    String update(UserUpdateDTO userUpdateDTO, String currentAccount);
    String updatePermission(String account, Integer permissionLevel);

    /**
     * 分页查询用户
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.UserDTO> findByPage(int page, int pageSize);

    /**
     * 按姓名模糊查询分页
     * @param name 姓名
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 分页响应
     */
    org.example.wms_backend.dto.PageResponseDTO<org.example.wms_backend.dto.UserDTO> findByNamePage(String name, int page, int pageSize);
}