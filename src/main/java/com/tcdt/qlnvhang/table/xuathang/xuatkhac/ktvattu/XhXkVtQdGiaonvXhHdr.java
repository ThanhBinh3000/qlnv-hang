package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
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
@Table(name = XhXkVtQdGiaonvXhHdr.TABLE_NAME)
public class XhXkVtQdGiaonvXhHdr extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_VT_QD_GIAONV_XH_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_VT_QD_GIAONV_XH_HDR_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_VT_QD_GIAONV_XH_HDR_SEQ", allocationSize = 1, name = "XH_XK_VT_QD_GIAONV_XH_HDR_SEQ")
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String soQuyetDinh;
    private String loai;
    private String trichYeu;
    private LocalDate ngayKy;
    private LocalDate thoiHanXuatHang;
    private String soCanCu;
    private Long idCanCu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;

    @OneToMany(mappedBy = "xhXkVtQdGiaonvXhHdr", cascade = CascadeType.ALL)
    private List<XhXkVtQdGiaonvXhDtl> xhXkVtQdGiaonvXhDtl = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoai;
}
