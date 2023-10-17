package com.tcdt.qlnvhang.entities.xuathang.bantructiep.xuatkho.phieuxuatkho;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = XhPhieuXkhoBtt.TABLE_NAME)
@Data
public class XhPhieuXkhoBtt implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_PHIEU_XKHO_BTT";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =TABLE_NAME +"_SEQ")
//  @SequenceGenerator(sequenceName = TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;
    private Integer namKh;
    private String maDvi;
    private String maQhNs;
    private String soPhieuXuatKho;
    private LocalDate ngayLapPhieu;
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhNx;
    private String kieuNx;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private Long idBangKeHang;
    private String soBangKeHang;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private Long idLanhDaoChiCuc;
    private String keToanTruong;
    private LocalDate tgianGiaoNhan;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private BigDecimal soLuongHopDong;
    private Long idBangKeBanLe;
    private String soBangKeBanLe;
    private LocalDate ngayTaoBkeBanLe;
    private BigDecimal soLuongBkeBanLe;
    private String maSo;
    private BigDecimal theoChungTu;
    private BigDecimal thucXuat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private String pthucBanTrucTiep; // 01 : chào giá; 02 : Ủy quyền; 03 : Bán lẻ
    private String phanLoai;
    private String trangThai;
    private String lyDoTuChoi;
    private LocalDate ngayTao;
    private Long nguoiTaoId;
    private LocalDate ngaySua;
    private Long nguoiSuaId;
    private LocalDate ngayGuiDuyet;
    private Long nguoiGuiDuyetId;
    private LocalDate ngayPduyet;
    private Long nguoiPduyetId;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenNganLoKho;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenKtvBaoQuan;
    @Transient
    private String tenLanhDaoChiCuc;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenBenMua;
    @Transient
    private String maDviCha;
    @Transient
    private String tenDviCha;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
        }
        if (!DataUtils.isNullObject(getMaDiemKho())) {
            setTenDiemKho(mapDmucDvi.containsKey(getMaDiemKho()) ? mapDmucDvi.get(getMaDiemKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNhaKho())) {
            setTenNhaKho(mapDmucDvi.containsKey(getMaNhaKho()) ? mapDmucDvi.get(getMaNhaKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNganKho())) {
            setTenNganKho(mapDmucDvi.containsKey(getMaNganKho()) ? mapDmucDvi.get(getMaNganKho()) : null);
            if (getTenNganKho() != null) {
                setTenNganLoKho(getTenNganKho());
            }
        }
        if (!DataUtils.isNullObject(getMaLoKho())) {
            setTenLoKho(mapDmucDvi.containsKey(getMaLoKho()) ? mapDmucDvi.get(getMaLoKho()) : null);
            if (getTenLoKho() != null) {
                setTenNganLoKho(getTenLoKho() + " - " + getTenNganKho());
            }
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapLoaiHinhNx;

    public void setMapLoaiHinhNx(Map<String, String> mapLoaiHinhNx) {
        this.mapLoaiHinhNx = mapLoaiHinhNx;
        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapLoaiHinhNx.containsKey(getLoaiHinhNx()) ? mapLoaiHinhNx.get(getLoaiHinhNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapKieuNhapXuat;

    public void setMapKieuNhapXuat(Map<String, String> mapKieuNhapXuat) {
        this.mapKieuNhapXuat = mapKieuNhapXuat;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapKieuNhapXuat.containsKey(getKieuNx()) ? mapKieuNhapXuat.get(getKieuNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapVthh;

    public void setMapVthh(Map<String, String> mapVthh) {
        this.mapVthh = mapVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapVthh.containsKey(getLoaiVthh()) ? mapVthh.get(getLoaiVthh()) : null);
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapVthh.containsKey(getCloaiVthh()) ? mapVthh.get(getCloaiVthh()) : null);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhPhieuXkhoBtt.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(s -> {
                s.setDataType(XhPhieuXkhoBtt.TABLE_NAME + "_CAN_CU");
                s.setXhPhieuXkhoBtt(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhPhieuXkhoBtt.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhPhieuXkhoBtt.TABLE_NAME + "_DINH_KEM");
                s.setXhPhieuXkhoBtt(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }
}