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
@Table(name = "QL_BANG_KE_CH_CT_LT")
public class QlBangKeChCtLt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QL_BANG_KE_CH_CT_LT_SEQ")
    @SequenceGenerator(sequenceName = "QL_BANG_KE_CH_CT_LT_SEQ", allocationSize = 1, name = "QL_BANG_KE_CH_CT_LT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QL_BANG_KE_CAN_HANG_LT_ID")
    private Long qlBangKeCanHangLtId;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "MA_CAN")
    private String maCan;

    @Column(name = "SO_BAO_BI")
    private BigDecimal soBaoBi;

    @Column(name = "TRONG_LUONG_CA_BI")
    private BigDecimal trongLuongCaBi;
}
