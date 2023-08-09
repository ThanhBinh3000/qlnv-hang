package com.tcdt.qlnvhang.table.xuathang.xuatkhac.xuathangkhoidm;

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
@Table(name = XhXkQdXuatHangKhoiDm.TABLE_NAME)
public class XhXkQdXuatHangKhoiDm extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_QD_XUAT_HANG_KHOI_DM";

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String soQd;
    private LocalDate ngayKy;
    private LocalDate ngayHieuLuc;
    private LocalDate ngayDuyet;
    private String maCanCu;
    private Long idCanCu;
    private String trichYeu;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    @Transient
    private List<XhXkDsHangDtqgDtl> listDtl = new ArrayList<>();
    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
}
