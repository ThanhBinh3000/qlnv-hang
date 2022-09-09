package com.tcdt.qlnvhang.entities.xuathang.phieuxuatkho;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = XhPhieuXuatKhoCt.TABLE_NAME)
@Data
public class XhPhieuXuatKhoCt {
    public static final String TABLE_NAME = "XH_PHIEU_XUAT_KHO_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_PHIEU_XUAT_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "XH_PHIEU_XUAT_KHO_CT_SEQ", allocationSize = 1, name = "XH_PHIEU_XUAT_KHO_CT_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long pxuatKhoId;
    @Column(name = "CHUNG_TU")
    private String chungTu;
    @Column(name = "TEN_FILE")
    private String tenFile;

}
