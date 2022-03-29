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
@Table(name = "QL_PHIEU_NHAP_KHO_HANG_HOA_LT")
public class QlPhieuNhapKhoHangHoaLt implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QL_PHIEU_NHAP_KHO_HANG_HOA_LT_SEQ")
    @SequenceGenerator(sequenceName = "QL_PHIEU_NHAP_KHO_HANG_HOA_LT_SEQ", allocationSize = 1, name = "QL_PHIEU_NHAP_KHO_HANG_HOA_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "QL_PHIEU_NHAP_KHO_LT_ID")
    private Long qlPhieuNhapKhoLtId;

    @Column(name = "VAT_TU_ID")
    private Long vatTuId;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG_TREN_CT")
    private BigDecimal soLuongTrenCt;

    @Column(name = "SO_LUONG_THUC")
    private BigDecimal soLuongThuc;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;
}
