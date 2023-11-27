package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HH_BIEN_BAN_LAY_MAU_DTL")
@Data
public class HhBbanLayMauDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_BIEN_BAN_LAY_MAU_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_BB_LAY_MAU_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_BB_LAY_MAU_DTL_SEQ", allocationSize = 1, name = "HH_BB_LAY_MAU_DTL_SEQ")

    private Long id;
    private Long idHdr;
    private String daiDien;
    private String loaiDaiDien;
    private String tenLoaiDaiDien;
}
