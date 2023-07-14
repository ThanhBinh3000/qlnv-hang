package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkVtBbLayMauDtl.TABLE_NAME)
@Data
public class XhXkVtBbLayMauDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_XK_VT_BB_LAY_MAU_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBbLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBbLayMauDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = XhXkVtBbLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private String daiDien;
    private String loaiDaiDien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHdr")
    @JsonIgnore
    private XhXkVtBbLayMauHdr xhXkVtBbLayMauHdr;
}
