package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangkecanhang;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NH_BANG_KE_CAN_HANG_CT")
public class NhBangKeCanHangCt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_CAN_HANG_LT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BANG_KE_CAN_HANG_LT_CT_SEQ", allocationSize = 1, name = "BANG_KE_CAN_HANG_LT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_BANG_KE_CAN_HANG_HDR")
    private Long idBangKeCanHangHdr;

    @Column(name = "MA_CAN")
    private String maCan;
    private String phanLoai;
    private Long lan;
    private BigDecimal soBaoDem;
    @Column(name = "TRONG_LUONG_BAO_BI")
    private BigDecimal trongLuongBaoBi;

    @Column(name = "TRONG_LUONG_CA_BAO_BI")
    private BigDecimal trongLuongCaBaoBi;
}
