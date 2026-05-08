package org.example.wms_backend.mapper;

import org.example.wms_backend.entity.OperationLog;
import org.example.wms_backend.entity.UserActivityStatistic;
import org.example.wms_backend.entity.OperationTypeStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志表Mapper接口
 */
@Mapper
public interface OperationLogMapper {
    /**
     * 插入操作日志
     * @param operationLog 操作日志实体
     * @return 影响行数
     */
    int insert(OperationLog operationLog);
    
    /**
     * 删除超过三天的操作日志
     * @return 影响行数
     */
    int deleteOldLogs();

    /**
     * 查询所有操作日志
     * @return 操作日志列表
     */
    List<OperationLog> selectAll();

    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLog> selectByOperationType(@Param("operationType") String operationType);

    /**
     * 根据用户账号查询操作日志
     * @param account 用户账号
     * @return 操作日志列表
     */
    List<OperationLog> selectByAccount(@Param("account") String account);

    // 用户活跃度统计
    List<UserActivityStatistic> selectUserActivityStatistics(@Param("days") int days);

    // 操作类型统计
    List<OperationTypeStatistic> selectOperationTypeStatistics(@Param("days") int days);

    // 分页查询操作日志
    List<OperationLog> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询操作日志总数
    int selectCount();

    // 根据操作类型模糊查询分页
    List<OperationLog> selectByOperationTypeLikePage(@Param("operationType") String operationType, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据操作类型模糊查询总数
    int selectCountByOperationTypeLike(@Param("operationType") String operationType);

    // 根据用户账号模糊查询分页
    List<OperationLog> selectByAccountLikePage(@Param("account") String account, @Param("offset") int offset, @Param("pageSize") int pageSize);

    // 根据用户账号模糊查询总数
    int selectCountByAccountLike(@Param("account") String account);
}