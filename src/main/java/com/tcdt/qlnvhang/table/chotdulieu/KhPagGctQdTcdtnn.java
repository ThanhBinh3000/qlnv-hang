package com.tcdt.qlnvhang.table.chotdulieu;


import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = KhPagGctQdTcdtnn.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhPagGctQdTcdtnn {
    public static final String TABLE_NAME = "KH_PAG_GCT_QD_TCDTNN";

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KH_PAG_GCT_QD_TCDTNN_SEQ")
    @SequenceGenerator(sequenceName = "KH_PAG_GCT_QD_TCDTNN_SEQ", allocationSize = 1, name = "KH_PAG_GCT_QD_TCDTNN_SEQ")
    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "MA_DVI")
    String maDvi;

    @Column(name = "TRANG_THAI")
    String trangThai;

    @Column(name = "NAM_KE_HOACH")
    Long namKeHoach;

    @Column(name = "SO_QD")
    String soQd;

    @Column(name = "NGAY_KY")
    LocalDate ngayKy;

    @Column(name = "NGAY_HIEU_LUC")
    LocalDate ngayHieuLuc;

    @Column(name = "SO_TO_TRINH")
    String soToTrinh;

    @Column(name = "LOAI_VTHH")
    String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    String cloaiVthh;

    @Column(name = "LOAI_GIA")
    String loaiGia;

    @Column(name = "TIEU_CHUAN_CL")
    String tchuanCluong;

    @Column(name = "TRICH_YEU")
    String trichYeu;

    @Column(name = "GHI_CHU")
    String ghiChu;

    @Column(name = "CAP_DVI")
    String capDvi;

    @Column(name = "NGUOI_PDUYET ")
    String nguoiPduyet;

    @Column(name = "NGAY_PDUYET ")
    Date ngayPduyet;

    @Column(name = "MO_TA_HANG_HOA")
    String moTaHangHoa;

    @Column(name = "SO_QD_DC")
    String soQdDc;

    @Column(name = "LOAI_DE_XUAT")
    String loaiDeXuat;

    @Column(name = "NGAY_TAO")
    LocalDateTime ngayTao;

    @Column(name = "NGUOI_TAO_ID")
    Long nguoiTaoId;

    @Column(name = "NGAY_SUA")
    LocalDateTime ngaySua;

    @Column(name = "NGUOI_SUA_ID")
    Long nguoiSuaId;

    @Transient
    String tenTrangThai;

    @Transient
    String tenLoaiVthh;

    @Transient
    String tenCloaiVthh;

    @Transient
    String tenLoaiGia;

    @Transient
    String tenDvi;

    @Transient
    List<KhPagTongHopCTiet> thongTinGiaLt;

    @Transient
    List<KhPagTtChung> thongTinGiaVt;

    @Transient
    List<FileDinhKem> fileDinhKems = new ArrayList<>();

    @Transient
    List<FileDinhKem> canCuPhapLys = new ArrayList<>();


    public KhPagGctQdTcdtnn(Long id, String maDvi, String trangThai, Long namKeHoach, String soQd, LocalDate ngayKy, LocalDate ngayHieuLuc, String soToTrinh, String loaiVthh, String cloaiVthh, String loaiGia, String tchuanCluong, String trichYeu, String ghiChu, String capDvi, String nguoiPduyet, Date ngayPduyet, String moTaHangHoa, String soQdDc, String loaiDeXuat, LocalDateTime ngayTao, Long nguoiTaoId, LocalDateTime ngaySua, Long nguoiSuaId) {
        this.id = id;
        this.maDvi = maDvi;
        this.trangThai = trangThai;
        this.namKeHoach = namKeHoach;
        this.soQd = soQd;
        this.ngayKy = ngayKy;
        this.ngayHieuLuc = ngayHieuLuc;
        this.soToTrinh = soToTrinh;
        this.loaiVthh = loaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.loaiGia = loaiGia;
        this.tchuanCluong = tchuanCluong;
        this.trichYeu = trichYeu;
        this.ghiChu = ghiChu;
        this.capDvi = capDvi;
        this.nguoiPduyet = nguoiPduyet;
        this.ngayPduyet = ngayPduyet;
        this.moTaHangHoa = moTaHangHoa;
        this.soQdDc = soQdDc;
        this.loaiDeXuat = loaiDeXuat;
        this.ngayTao = ngayTao;
        this.nguoiTaoId = nguoiTaoId;
        this.ngaySua = ngaySua;
        this.nguoiSuaId = nguoiSuaId;
    }
}
