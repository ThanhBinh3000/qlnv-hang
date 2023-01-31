package com.tcdt.qlnvhang.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = UserActivity.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity implements Serializable {
    private static final long serialVersionUID = 8195707546894058286L;
    public static final String TABLE_NAME = "USER_ACTIVITY";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ACTIVITY_SEQ")
    @SequenceGenerator(sequenceName = "USER_ACTIVITY_SEQ", allocationSize = 1, name = "USER_ACTIVITY_SEQ")
    private Long id;

    private Long userId;
    private String userAgent;
    private String ip;
    private String requestMethod;
    private String requestUrl;
    private String requestParameter;
    private Date ngayTao;
    private String system;
    private String requestBody;
    private String userName;
}
