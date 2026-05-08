package org.example.wms_backend.service;

import java.util.List;

public interface SystemSettingsService {
    List<String> getHomepageComponents();
    String setHomepageComponents(List<String> components);
    String resetHomepageComponents();
}
