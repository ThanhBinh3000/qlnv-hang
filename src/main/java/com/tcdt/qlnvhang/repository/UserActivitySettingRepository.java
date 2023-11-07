package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.UserActivitySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivitySettingRepository extends JpaRepository<UserActivitySetting, Long> {
}
