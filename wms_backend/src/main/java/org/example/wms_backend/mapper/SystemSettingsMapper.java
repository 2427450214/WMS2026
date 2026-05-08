package org.example.wms_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.wms_backend.entity.SystemSettings;

@Mapper
public interface SystemSettingsMapper {
    SystemSettings selectByKey(@Param("settingKey") String settingKey);
    int insert(SystemSettings settings);
    int updateByKey(SystemSettings settings);
}
