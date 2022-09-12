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

    @Column(name = "TEN")
    private String ten;
    @Column(name = "MA_SO")
    private String maSo;
    @Column(name = "DON_VI_TINH")
    private String dvTinh;
    @Column(name = "SL_YEU_CAU")
    private Integer slYeuCau;
    @Column(name = "SL_THUC_XUAT")
    private Integer slThucXuat;
    @Column(name = "DON_GIA")
    private Integer donGia;
    @Column(name = "THANH_TIEN")
    private Integer thanhTien;
}
