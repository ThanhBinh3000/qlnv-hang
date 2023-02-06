package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_QD_GIAO_NV_XH_DDIEM")
@Data
public class XhQdGiaoNvXhDdiem {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_NH_DDIEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_GIAO_NV_NH_DDIEM_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_GIAO_NV_NH_DDIEM_SEQ", allocationSize = 1, name = "XH_QD_GIAO_NV_NH_DDIEM_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private String maDviTsan;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;

//    @Transient
//    List<HhPhieuKiemTraChatLuong> listPhieuKtraCl;
//
//    @Transient
//   List<HhPhieuNhapKhoHdr>  hhPhieuNhapKhoHdr;
//
//    @Transient
//    List<HhBcanKeHangHdr> bcanKeHangHdr;
//
//    @Transient
//    List<HhBienBanDayKhoHdr> bienBanNhapDayKho;

}

