package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = HhBbNghiemThuNhapKhacDtl.TABLE_NAME)
@Data
public class HhBbNghiemThuNhapKhacDtl {
    public static final String TABLE_NAME = "HH_BB_NGHIEM_THU_NHAP_KHAC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BBNT_NHAP_KHAC_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BBNT_NHAP_KHAC_DTL_SEQ", allocationSize = 1, name = "HH_BBNT_NHAP_KHAC_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String type;
    private String noiDung;
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    private String donViTinhMh;
    private BigDecimal tongGiaTri;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGia;
    private BigDecimal thanhTienTrongNam;
    private BigDecimal soLuongNamTruoc;
    private BigDecimal thanhTienNamTruoc;
    private Boolean isParent;
    private String idParent;
}
