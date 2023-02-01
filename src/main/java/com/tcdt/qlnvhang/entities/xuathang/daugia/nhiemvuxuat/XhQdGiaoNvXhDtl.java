package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_GIAO_NV_XH_DTL")
@Data
public class XhQdGiaoNvXhDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_NHAP_HANG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_GIAO_NV_NH_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_GIAO_NV_NH_DTL_SEQ", allocationSize = 1, name = "XH_QD_GIAO_NV_NH_DTL_SEQ")
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private String trangThai;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private List<XhQdGiaoNvXhDdiem> hhQdGiaoNvNhDdiemList = new ArrayList<>();
//    @Transient
//    List<HhBienBanNghiemThu> listBienBanNghiemThuBq;
//    @Transient
//    List<HhBienBanLayMau> listBienBanLayMau;
//    @Transient
//    List<HhPhieuKngiemCluong> listPhieuKiemNghiemCl;

}
