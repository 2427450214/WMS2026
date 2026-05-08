package org.example.wms_backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.wms_backend.entity.SystemSettings;
import org.example.wms_backend.mapper.SystemSettingsMapper;
import org.example.wms_backend.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Autowired
    private SystemSettingsMapper systemSettingsMapper;

    private static final String HOMEPAGE_COMPONENTS_KEY = "homepage:components";
    private static final List<String> DEFAULT_COMPONENTS = List.of("dailyInbound", "expiredWarning", "topStock", "dailyOutbound");
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> getHomepageComponents() {
        try {
            SystemSettings settings = systemSettingsMapper.selectByKey(HOMEPAGE_COMPONENTS_KEY);
            if (settings != null && settings.getSettingValue() != null) {
                return objectMapper.readValue(
                    settings.getSettingValue(), 
                    new TypeReference<List<String>>() {}
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_COMPONENTS;
    }

    @Override
    public String setHomepageComponents(List<String> components) {
        if (components == null || components.isEmpty()) {
            return "组件列表不能为空";
        }
        if (components.size() > 4) {
            return "最多只能选择4个组件";
        }
        try {
            String value = objectMapper.writeValueAsString(components);
            SystemSettings settings = systemSettingsMapper.selectByKey(HOMEPAGE_COMPONENTS_KEY);
            if (settings == null) {
                settings = new SystemSettings(HOMEPAGE_COMPONENTS_KEY, value);
                systemSettingsMapper.insert(settings);
            } else {
                settings.setSettingValue(value);
                systemSettingsMapper.updateByKey(settings);
            }
            return "设置保存成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "保存失败";
        }
    }

    @Override
    public String resetHomepageComponents() {
        try {
            String value = objectMapper.writeValueAsString(DEFAULT_COMPONENTS);
            SystemSettings settings = systemSettingsMapper.selectByKey(HOMEPAGE_COMPONENTS_KEY);
            if (settings == null) {
                settings = new SystemSettings(HOMEPAGE_COMPONENTS_KEY, value);
                systemSettingsMapper.insert(settings);
            } else {
                settings.setSettingValue(value);
                systemSettingsMapper.updateByKey(settings);
            }
            return "已重置为默认配置";
        } catch (Exception e) {
            e.printStackTrace();
            return "重置失败";
        }
    }
}
