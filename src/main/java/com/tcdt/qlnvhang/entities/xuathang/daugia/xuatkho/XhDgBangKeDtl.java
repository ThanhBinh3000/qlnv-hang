package com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = XhDgBangKeDtl.TABLE_NAME)
@Data
public class XhDgBangKeDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DG_BANG_KE_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBangKeDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhDgBangKeDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDgBangKeDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maCan;
    private BigDecimal soBaoBi;
    private BigDecimal trongLuongCaBi;
    private String loai;
    private BigDecimal idVirtual;
}
