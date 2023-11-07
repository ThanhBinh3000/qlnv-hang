package com.tcdt.qlnvhang.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = UserActivitySetting.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivitySetting implements Serializable {
    public static final String TABLE_NAME = "USER_ACTIVITY_SETTING";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ACTIVITY_SETTING_SEQ")
    @SequenceGenerator(sequenceName = "USER_ACTIVITY_SETTING_SEQ", allocationSize = 1, name = "USER_ACTIVITY_SETTING_SEQ")
    private Long id;

    private Boolean writeLogAll;
    private Boolean writeLogLogin;
    private Boolean writeLogLogout;
    private Boolean writeLogUserActivity;
    private LocalDateTime ngayTao;
    private LocalDateTime ngaySua;
}
