package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhTlBbLayMauDtl.TABLE_NAME)
@Data
public class XhTlBbLayMauDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TL_BB_LAY_MAU_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlBbLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhTlBbLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlBbLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;

    private String hoVaTen;

    private String daiDien;
}
