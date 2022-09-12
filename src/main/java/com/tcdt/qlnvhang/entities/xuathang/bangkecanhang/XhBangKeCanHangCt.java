package com.tcdt.qlnvhang.entities.xuathang.bangkecanhang;

import lombok.Data;
import org.joda.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = XhBangKeCanHangCt.TABLE_NAME)
@Data
public class XhBangKeCanHangCt {
    public static final String TABLE_NAME = "XH_BANG_KE_CAN_HANG_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BANG_KE_CAN_HANG_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_BANG_KE_CAN_HANG_CT_SEQ", allocationSize = 1, name = "XH_BANG_KE_CAN_HANG_CT_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "BANG_KE_CAN_HANG_ID")
    private Long bkCanHangID;

    @Column(name = "NGUOI_TAO_ID")
    private Long nguoiTaoId;
    @Column(name = "NGAY_TAO")
    private LocalDate ngayTao;
    @Column(name = "NGUOI_SUA_ID")
    private Long nguoiSuaId;
    @Column(name = "NGAY_SUA")
    private LocalDate ngaySua;

    @Column(name = "SO_BB")
    private Integer soBB;

    @Column(name = "TL_CA_BI")
    private Integer tlCaBi;


}
