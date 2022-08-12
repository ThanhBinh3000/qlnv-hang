package com.tcdt.qlnvhang.entities.xuathang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "XH_QD_GIAO_NVU_XUAT_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhQdGiaoNvuXuatCt implements Serializable {
    private static final long serialVersionUID = -8214262247395355756L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_XUAT_CT_SEQ")
    @SequenceGenerator(sequenceName = "QD_GIAO_NVU_XUAT_CT_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_XUAT_CT_SEQ")
    private Long id;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_NGAN_LO")
    private String maNganLo;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "DON_GIA_KHONG_THUE")
    private BigDecimal donGiaKhongThue;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;

    @Column(name = "THOI_HAN_XUAT_BAN")
    private LocalDate thoiHanXuatBan;

    @Column(name = "QDGNVX_ID")
    private Long qdgnvxId;
}
