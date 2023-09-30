package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

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
@Table(name = XhTlBangKeDtl.TABLE_NAME)
public class XhTlBangKeDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BANG_KE_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBangKeDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlBangKeDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBangKeDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maCan;
    private BigDecimal soBaoBi;
    private BigDecimal trongLuongCaBi;
}
