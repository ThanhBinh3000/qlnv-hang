package com.tcdt.qlnvhang.entities.quanlybienbannhapdaykholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NH_BB_NHAP_DAY_KHO_LT_CT")
public class QlBienBanNdkCtLt implements Serializable {
    private static final long serialVersionUID = -6188737070007624709L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NHAP_DAY_KHO_LT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_NHAP_DAY_KHO_LT_CT_SEQ", allocationSize = 1, name = "BB_NHAP_DAY_KHO_LT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "QL_BIEN_BAN_NDK_LT_ID")
    private Long qlBienBanNdkLtId; // QL_BIEN_BAN_NHAP_DAY_KHO_LT

    @Column(name = "STT")
    private Integer stt;
}
