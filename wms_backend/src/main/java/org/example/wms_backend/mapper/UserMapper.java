package org.example.wms_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.wms_backend.entity.User;
import java.util.List;

@Mapper
public interface UserMapper {
    int insert(User user);
    User selectByAccount(String account);
    List<User> selectByName(String name);
    List<User> selectByAccountStatus(String accountStatus);
    List<User> selectByPermissionLevel(int permissionLevel);
    Integer selectPermissionLevelByAccount(String account);
    int updateIsDeletedByAccount(String account);
    List<User> selectAll();
    int updateByPrimaryKey(User user);

    // 分页查询用户
    List<User> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询用户总数
    int selectCount();

    // 按姓名模糊查询分页
    List<User> selectByNamePage(@Param("name") String name, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 按姓名模糊查询总数
    int selectCountByName(@Param("name") String name);
}