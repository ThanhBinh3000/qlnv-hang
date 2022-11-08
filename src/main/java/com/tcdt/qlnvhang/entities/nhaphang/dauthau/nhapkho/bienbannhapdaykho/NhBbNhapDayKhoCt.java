package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbannhapdaykho;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NH_BB_NHAP_DAY_KHO_CT")
public class NhBbNhapDayKhoCt implements Serializable {
    private static final long serialVersionUID = -6188737070007624709L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_NHAP_DAY_KHO_LT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_NHAP_DAY_KHO_LT_CT_SEQ", allocationSize = 1, name = "BB_NHAP_DAY_KHO_LT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_BB_NHAP_DAY_KHO")
    private Long idBbNhapDayKho; // NH_BB_NHAP_DAY_KHO_LT

    @Column(name = "SO_PHIEU_KTRA_CL")
    private String soPhieuKtraCl;

    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;
}
