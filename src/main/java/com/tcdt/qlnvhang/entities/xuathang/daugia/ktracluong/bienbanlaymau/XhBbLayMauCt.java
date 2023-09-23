package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhBbLayMauCt.TABLE_NAME)
@Data
public class XhBbLayMauCt implements Serializable {
    public static final String TABLE_NAME = "XH_BB_LAY_MAU_CT";
    private static final long serialVersionUID = 5219010961728948644L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBbLayMauCt.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhBbLayMauCt.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBbLayMauCt.TABLE_NAME + "_SEQ")
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
