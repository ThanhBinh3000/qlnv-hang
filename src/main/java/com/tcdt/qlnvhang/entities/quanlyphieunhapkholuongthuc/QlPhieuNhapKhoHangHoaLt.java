package com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc;

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
@Table(name = "NH_PHIEU_NHAP_KHO_LT_CT")
public class QlPhieuNhapKhoHangHoaLt implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_LT_CT_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_LT_CT_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_LT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "QL_PHIEU_NHAP_KHO_LT_ID")
    private Long qlPhieuNhapKhoLtId;

    @Column(name = "MA_SO")
    private String maSo;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG_TREN_CT")
    private BigDecimal soLuongTrenCt;

    @Column(name = "SO_LUONG_THUC_NHAP")
    private BigDecimal soLuongThucNhap;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;
}
