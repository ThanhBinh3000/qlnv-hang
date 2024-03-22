package com.tcdt.qlnvhang.table.nhaphangtheoptt;

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
@Table(name = "HH_BCAN_KE_HANG_DTL")
public class HhBcanKeHangDtl  implements Serializable {
    private static final long serialVersionUID = 3529822360093876437L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BCAN_KE_HANG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BCAN_KE_HANG_DTL_SEQ", allocationSize = 1, name = "HH_BCAN_KE_HANG_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String maCan;

    private BigDecimal soBaoBi;

    private BigDecimal trongLuongCaBi;
    private BigDecimal trongLuongBaoBi;
    private String phanLoai;
    private Long lan;
    private BigDecimal soBaoDem;

}
