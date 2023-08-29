package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NH_PHIEU_NHAP_KHO_CT")
public class NhPhieuNhapKhoCt implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_CT_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_PHIEU_NK_HDR")
    private Long idPhieuNkHdr;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "MO_TA_HANG_HOA")
    private String moTaHangHoa;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG_CHUNG_TU")
    private BigDecimal soLuongChungTu;

    @Column(name = "SO_LUONG_THUC_NHAP")
    private BigDecimal soLuongThucNhap;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;

    @Column(name = "MA_SO")
    private String maSo;
    @Transient
    private BigDecimal thanhTien;

}
