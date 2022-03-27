package com.tcdt.qlnvhang.entities.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QLHDMVT_THONG_TIN_CHUNG")
public class QlhdmvtThongTinChung extends BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NHL_TU_NGAY")
    private LocalDate ngayHieuLucTuNgay;

    @Column(name = "NHL_DEN_NGAY")
    private Long ngayHieuLucDenNgay;

    @Column(name = "SO_NGAY_TH")
    private Long soNgayTh;

    @Column(name = "LOAI_HANG_ID")
    private Long loaiHangId;

    @Column(name = "TOC_DO_CC_TU_NGAY")
    private LocalDate tocDoCcTuNgay;

    @Column(name = "TOC_DO_CC_DEN_NGAY")
    private LocalDate tocDoCcDenNgay;

    @Column(name = "SO_NGAY_THEO_TEN_DO")
    private Long soNgayTheoTenDo;

    @Column(name = "LOAI_HOP_DONG_ID")
    private Long loaiHopDongId;

    @Column(name = "NUOC_SAN_XUAT_ID")
    private Long nuocSanXuatId;

    @Column(name = "TIEU_CHUAN_CL")
    private String tieuChuanCl;

    @Column(name = "SO_LUONG")
    private Long soLuong;

    @Column(name = "GIA_TRI_HD_TRUOC_THUE")
    private BigDecimal giaTriHdTruocThue;

    @Column(name = "THUE_VAT")
    private Integer thueVat;

    @Column(name = "GIA_TRI_HD_SAU_THU")
    private BigDecimal giaTriHdSauThu;

    @Column(name = "FILE_DINH_KEM_ID")
    private Long fileDinhKemId;

    @Column(name = "CHU_DAU_TU_ID")
    private Long chuDauTuId;

    @Column(name = "DV_CUNG_CAP_ID")
    private Long dvCungCapId;

    @Column(name = "GHI_CHU")
    private String ghiChu;
}
