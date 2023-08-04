package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhXkVtBhQdXuatGiamVt.TABLE_NAME)
public class XhXkVtBhQdXuatGiamVt extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_VT_BH_QD_XG_VT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhQdXuatGiamVt.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhXkVtBhQdXuatGiamVt.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXkVtBhQdXuatGiamVt.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String maDviNhan;
    private String soQuyetDinh;
    private String loai;
    private String trichYeu;
    private LocalDate ngayKy;
    private LocalDate thoiHanXuatGiam;
    private String soCanCu;
    private Long idCanCu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String listSoQdGiaoNvXh;
    private String listIdQdGiaoNvXh;

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenDviNhan;
    @Transient
    private String tenLoai;

    @OneToMany(mappedBy = "qdXuatGiamVt", cascade = CascadeType.ALL)
    private List<XhXkVtBhQdXuatGiamVtDtl>  qdXuatGiamVtDtl = new ArrayList<>();

}
