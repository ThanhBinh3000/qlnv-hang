package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = XhTlQdGiaoNvDtl.TABLE_NAME)
@Data
public class XhTlQdGiaoNvDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_QD_GIAO_NV_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQdGiaoNvDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlQdGiaoNvDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlQdGiaoNvDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private Long idDsHdr;

    private String phanLoai;

    @Transient
    private XhTlDanhSachHdr xhTlDanhSachHdr;
}
