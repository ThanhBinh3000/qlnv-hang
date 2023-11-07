package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.repository.UserActivitySettingRepository;
import com.tcdt.qlnvhang.table.UserActivitySetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActivitySettingService {
    @Autowired
    private UserActivitySettingRepository userActivitySettingRepository;

    public UserActivitySetting save(UserActivitySetting objReq) throws Exception {
        List<UserActivitySetting> userActivitySettings = userActivitySettingRepository.findAll();
        UserActivitySetting setting = null;
        if (userActivitySettings.isEmpty()) {
            setting = new UserActivitySetting();
            setting.setNgayTao(LocalDateTime.now());
        } else {
            setting = userActivitySettings.get(0);
        }
        setting.setWriteLogAll(objReq.getWriteLogAll());
        setting.setWriteLogLogin(objReq.getWriteLogLogin());
        setting.setWriteLogLogout(objReq.getWriteLogLogout());
        setting.setWriteLogUserActivity(objReq.getWriteLogUserActivity());
        setting.setNgaySua(LocalDateTime.now());
        setting = userActivitySettingRepository.save(setting);
        return setting;
    }

    public UserActivitySetting getSetting() throws Exception {
        List<UserActivitySetting> userActivitySettings = userActivitySettingRepository.findAll();
        UserActivitySetting setting = null;
        if (userActivitySettings.isEmpty()) {
            setting = new UserActivitySetting();
            setting.setWriteLogAll(false);
            setting.setWriteLogLogin(false);
            setting.setWriteLogLogout(false);
            setting.setWriteLogUserActivity(false);
        } else {
            setting = userActivitySettings.get(0);
        }
        return setting;
    }
}