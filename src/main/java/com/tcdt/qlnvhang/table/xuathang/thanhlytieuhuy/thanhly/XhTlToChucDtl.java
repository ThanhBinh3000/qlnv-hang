package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = XhTlToChucDtl.TABLE_NAME)
@Data
public class XhTlToChucDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_TO_CHUC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlToChucDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlToChucDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlToChucDtl.TABLE_NAME + "_SEQ")
    private Long id;

    private Long idHdr;

    private Long idDsHdr;

    private String maDviTsan;

    private Integer soLanTraGia;

    private BigDecimal donGiaCaoNhat;

    private String toChucCaNhan;

    @Transient
    private XhTlDanhSachHdr xhTlDanhSachHdr;

}
