package com.tcdt.qlnvhang.entities.xuathang.bbtinhkho;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = XhBienBanTinhKhoCt.TABLE_NAME)
@Data
public class XhBienBanTinhKhoCt {
    public static final String TABLE_NAME = "XH_BB_TINH_KHO_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_TINH_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_BB_TINH_KHO_CT_SEQ", allocationSize = 1, name = "XH_BB_TINH_KHO_CT_SEQ")
    private Long id;
    private Long bbTinhKhoId;
    private String daiDien;
    private int stt;
    private String chuVu;
    private String maDvi;
    private String tenDvi;

}
