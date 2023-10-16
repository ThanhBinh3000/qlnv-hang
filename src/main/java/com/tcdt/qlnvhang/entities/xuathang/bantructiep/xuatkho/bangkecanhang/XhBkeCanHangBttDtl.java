package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.bangkecanhang;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhBkeCanHangBttDtl.TABLE_NAME)
@Data
public class XhBkeCanHangBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BKE_CAN_HANG_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maCan;
    private String soBaoBi;
    private BigDecimal trongLuongCaBi;
}