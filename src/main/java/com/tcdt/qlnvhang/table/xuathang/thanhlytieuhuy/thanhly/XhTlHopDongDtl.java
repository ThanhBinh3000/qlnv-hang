package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = XhTlHopDongDtl.TABLE_NAME)
@Data
public class XhTlHopDongDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_HOP_DONG_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlHopDongDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlHopDongDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhTlHopDongDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDviTsan;
    private Long idDsHdr;

}
