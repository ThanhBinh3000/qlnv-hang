package com.tcdt.qlnvhang.entities.quanlybangkecanhangluongthuc;

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
@Table(name = "NH_BANG_KE_CAN_HANG_LT_CT")
public class QlBangKeChCtLt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_CAN_HANG_LT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BANG_KE_CAN_HANG_LT_CT_SEQ", allocationSize = 1, name = "BANG_KE_CAN_HANG_LT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QL_BANG_KE_CAN_HANG_LT_ID")
    private Long qlBangKeCanHangLtId;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "SO_BAO_BI")
    private BigDecimal soBaoBi;

    @Column(name = "TRONG_LUONG_CA_BI")
    private BigDecimal trongLuongCaBi;
}
