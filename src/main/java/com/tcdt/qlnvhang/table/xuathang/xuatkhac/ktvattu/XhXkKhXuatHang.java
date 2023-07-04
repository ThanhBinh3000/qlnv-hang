package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhXkKhXuatHang.TABLE_NAME)
public class XhXkKhXuatHang extends BaseEntity implements Serializable {

    public static final String TABLE_NAME = "XH_XK_KH_XUATHANG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_XK_KH_XUATHANG_SEQ")
    @SequenceGenerator(sequenceName = "XH_XK_KH_XUATHANG_SEQ", allocationSize = 1, name = "XH_XK_KH_XUATHANG_SEQ")
    private Long id;
    private Integer namKeHoach;
    private String maDvi;
    private String diaChi;
    private String loaiHinhNhapXuat;
    private String kieuNhapXuat;
    private String soToTrinh;
    private String trichYeu;
    private LocalDate ngayKeHoach;
    private LocalDate ngayDuyetKeHoach;
    private LocalDate thoiGianDuKienXuatTu;
    private LocalDate thoiGianDuKienXuatDen;
    private String moTa;
    private String maTongHopDs;
    private Long idTongHopDs;
    private String trangThai;

    @OneToMany(mappedBy = "XhXkKhXuatHang", cascade = CascadeType.ALL)
    private List<XhXkKhXuatHangDtl> xhXkKhXuatHangDtl = new ArrayList<>();

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private Integer soDvTaiSan;
}
