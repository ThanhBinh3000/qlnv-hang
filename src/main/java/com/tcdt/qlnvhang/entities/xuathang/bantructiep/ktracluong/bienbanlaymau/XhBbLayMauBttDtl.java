package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_BB_LAY_MAU_BTT_DTL")
@Data
public class XhBbLayMauBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_LAY_MAU_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_BB_LAY_MAU_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_BB_LAY_MAU_BTT_DTL_SEQ", allocationSize = 1, name = "XH_BB_LAY_MAU_BTT_DTL_SEQ")
    private Long id;

    private Long idHdr;

    private String loaiDaiDien;

    private String daiDien;
}
