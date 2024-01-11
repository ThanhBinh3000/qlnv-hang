package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.response.xuathang.xuatkhac.ktvattu.XhXkTongHopKhXuatCuc;
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
    private LocalDate ngayDuyetBtc;
    private LocalDate ngayTrinhDuyetBtc;
    private LocalDate thoiGianDuKienXuatTu;
    private LocalDate thoiGianDuKienXuatDen;
    private String moTa;
    private String maTongHopDs;
    private Long idTongHopDs;
    private String trangThai;
    private String lyDoTuChoi;
    private Long nguoiDuyetId;
    private LocalDate ngayDuyet;
    private Integer capDvi;
    private String soQdBtc;
    private LocalDate ngayDxXuatHangTu;
    private LocalDate ngayDxXuatHangDen;

    @OneToMany(mappedBy = "XhXkKhXuatHang", cascade = CascadeType.ALL)
    private List<XhXkKhXuatHangDtl> xhXkKhXuatHangDtl = new ArrayList<>();

    //Entity Tổng hợp kế hoạch xuất hàng
    private String loai;
    private LocalDate thoiGianTh;
    private String noiDungTh;
    private String ghiChu;
    //Dùng cho Tổng hợp và kế hoạch của Tổng cục - TH thì là ID bản ghi kế hoạch của Cục, KH của Tổng cục là ID của bản ghi TH
    private Long idCanCu;

    @Transient
    private List<FileDinhKem> fileDinhKems;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDvi;
    @Transient
    private Integer soDviTaiSan;
    @Transient
    private List<XhXkTongHopKhXuatCuc> listDxCuc;
    @Transient
    private List<String> listSoKeHoachs;
    @Transient
    private List<Long> listIdKeHoachs;
}
