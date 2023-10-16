package com.tcdt.qlnvhang.entities.xuathang.bantructiep.ktracluong.bienbanlaymau;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name =XhBbLayMauBttDtl.TABLE_NAME)
@Data
public class XhBbLayMauBttDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_LAY_MAU_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBbLayMauBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhBbLayMauBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBbLayMauBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String ten;
    private String loai;
    private Integer ma;
    private String chiSoCl;
    private String phuongPhap;
    private Boolean checked;
    private String type;
}