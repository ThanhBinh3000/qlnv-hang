package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

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
@Table(name = XhXkVtQdXuatGiamVattu.TABLE_NAME)
public class XhXkVtQdXuatGiamVattu extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_VT_QD_XUAT_GIAM_VT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_VT_QD_XUAT_GIAM_VT_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_VT_QD_XUAT_GIAM_VT_SEQ", allocationSize = 1, name = "XH_XK_VT_QD_XUAT_GIAM_VT_SEQ")
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
    private LocalDate ngayGduyet;
    private Long nguoiGduyetId;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private String soQdGiaoNvXh;
    private Long idQdGiaoNvXh;

    @Transient
    private List<XhXkVtPhieuXuatNhapKho> xhXkVtPhieuXuatNhapKho = new ArrayList<>();
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
}
