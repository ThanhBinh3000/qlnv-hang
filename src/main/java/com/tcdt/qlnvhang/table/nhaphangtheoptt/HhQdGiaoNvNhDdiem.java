package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuktracl.NhPhieuKtChatLuong;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho.NhBbNhapDayKho;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HH_QD_GIAO_NV_NH_DDIEM")
@Data
public class HhQdGiaoNvNhDdiem {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_QD_GIAO_NV_NH_DDIEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_QD_GIAO_NV_NH_DDIEM_SEQ")
    @SequenceGenerator(sequenceName = "HH_QD_GIAO_NV_NH_DDIEM_SEQ", allocationSize = 1, name = "HH_QD_GIAO_NV_NH_DDIEM_SEQ")
    private Long id;
    private Long idDtl;
    private String maCuc;
    private String maChiCuc;
    private String MaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    @Transient
    private String tenCuc;
    @Transient
    private String tenChiCuc;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;

    @Transient
    List<HhPhieuKiemTraChatLuong> listPhieuKtraCl;

    @Transient
   List<HhPhieuNhapKhoHdr>  hhPhieuNhapKhoHdr;

    @Transient
    List<HhBcanKeHangHdr> bcanKeHangHdr;

    @Transient
    List<HhBienBanDayKhoHdr> bienBanNhapDayKho;
    @Transient
    List<HhBienBanNghiemThu> listBienBanNghiemThuBq;
    @Transient
    List<HhBienBanLayMau> listBienBanLayMau;
    @Transient
    List<HhPhieuKngiemCluong> listPhieuKiemNghiemCl;

//    preview
    @Transient
    private BigDecimal tongThanhTien;
    @Transient
    private BigDecimal donGia;
}

