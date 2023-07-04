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
    Long id;
    Integer namKeHoach;
    String maDvi;
    String diaChi;
    String loaiHinhNhapXuat;
    String kieuNhapXuat;
    String soToTrinh;
    String trichYeu;
    LocalDate ngayKeHoach;
    LocalDate ngayDuyetKeHoach;
    LocalDate thoiGianDuKienXuat;
    String moTa;
    String maTongHopDs;
    Long idTongHopDs;
    String trangThai;
    @OneToMany(mappedBy = "XhXkKhXuatHang", cascade = CascadeType.ALL)
    private List<XhXkKhXuatHangDtl> listXhXkKhXuatHangDtl = new ArrayList<>();
    @Transient
    List<FileDinhKem> fileDinhKems;
    @Transient
    String tenTrangThai;
    @Transient
    String tenDvi;
    @Transient
    Integer soDvTaiSan;
}
