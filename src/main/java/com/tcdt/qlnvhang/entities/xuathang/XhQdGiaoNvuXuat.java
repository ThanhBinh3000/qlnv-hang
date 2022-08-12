package com.tcdt.qlnvhang.entities.xuathang;

import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdGiaoNvuXuat.TABLE_NAME)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XhQdGiaoNvuXuat extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = 1036129798681529157L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NVU_XUAT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_XUAT_SEQ")
    @SequenceGenerator(sequenceName = "QD_GIAO_NVU_XUAT_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_XUAT_SEQ")
    private Long id;

    @Column(name = "SO_QUYET_DINH")
    private String soQuyetDinh;

    @Column(name = "NGAY_QUYET_DINH")
    private LocalDate ngayQuyetDinh;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "NAM_XUAT")
    private Integer namXuat;

    @Column(name = "TRICH_YEU")
    private String trichYeu;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    
    @Transient
    private List<XhQdGiaoNvuXuatCt> cts = new ArrayList<>();

    @Transient
    private List<XhQdGiaoNvuXuatCt1> ct1s = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems = new ArrayList<>();
}
