package org.example.wms_backend.controller;

import org.example.wms_backend.common.ResponseResult;
import org.example.wms_backend.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统设置控制器
 */
@RestController
@RequestMapping("/system")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    /**
     * 获取首页组件配置
     */
    @GetMapping("/settings/homepage")
    public Map<String, Object> getHomepageComponents() {
        List<String> components = systemSettingsService.getHomepageComponents();
        return ResponseResult.success(components);
    }

    /**
     * 设置首页组件配置
     */
    @PostMapping("/settings/homepage")
    public Map<String, Object> setHomepageComponents(@RequestBody List<String> components) {
        String result = systemSettingsService.setHomepageComponents(components);
        if (result.contains("成功")) {
            return ResponseResult.success(result);
        } else {
            return ResponseResult.error(result);
        }
    }

    /**
     * 重置为默认配置
     */
    @PostMapping("/settings/homepage/reset")
    public Map<String, Object> resetHomepageComponents() {
        String result = systemSettingsService.resetHomepageComponents();
        if (result.contains("成功")) {
            return ResponseResult.success(result);
        } else {
            return ResponseResult.error(result);
        }
    }
}
