package com.tcdt.qlnvhang.entities.xuathang;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "XH_QD_GIAO_NVU_XUAT_CT1")
@Data
@NoArgsConstructor
public class XhQdGiaoNvuXuatCt1 implements Serializable {
    private static final long serialVersionUID = 2130450505957225039L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_XUAT_CT1_SEQ")
    @SequenceGenerator(sequenceName = "QD_GIAO_NVU_XUAT_CT1_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_XUAT_CT1_SEQ")
    private Long id;

    @Column(name = "QDGNVX_ID")
    private Long qdgnvxId;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

}
