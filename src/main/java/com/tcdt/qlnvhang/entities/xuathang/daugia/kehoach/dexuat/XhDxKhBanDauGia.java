package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

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
import java.util.*;

@Entity
@Table(name = XhDxKhBanDauGia.TABLE_NAME)
@Data
public class XhDxKhBanDauGia implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxKhBanDauGia.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhDxKhBanDauGia.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDxKhBanDauGia.TABLE_NAME + "_SEQ")
    private Long id;
    private String maDvi;
    private String loaiHinhNx;
    private String kieuNx;
    private String diaChi;
    private Integer namKh;
    private String soDxuat;
    private String trichYeu;
    private Long idSoQdCtieu;
    private String soQdCtieu;
    private String loaiVthh;
    private String cloaiVthh;
    private String moTaHangHoa;
    private String tchuanCluong;
    private LocalDate tgianDkienTu;
    private LocalDate tgianDkienDen;
    private String ghiChuTgianDkien;
    private Integer tgianTtoan;
    private Integer tgianGnhan;
    private String pthucTtoan;
    private String pthucGnhan;
    private String tgianTtoanGhiChu;
    private String tgianGnhanGhiChu;
    private String thongBao;
    private BigDecimal khoanTienDatTruoc;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienKhoiDiemDx;
    private BigDecimal tongTienDatTruocDx;
    private String ghiChu;
    private Integer slDviTsan;
    private String trangThaiTh;
    private Long idSoQdPd;
    private String soQdPd;
    private Long idThop;
    private LocalDate ngayKyQd;
    private String donViTinh;
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
    private BigDecimal tongTienDuocDuyet;
    @Transient
    private BigDecimal tongKtienDtruocDduyet;
    @Transient
    private String tenDvi;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNx;
    @Transient
    private String tenPthucTtoan;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThaiTh;
    @Transient
    private String tenTrangThai;

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucDvi;

    public void setMapDmucDvi(Map<String, String> mapDmucDvi) {
        this.mapDmucDvi = mapDmucDvi;
        if (!DataUtils.isNullObject(getMaDvi())) {
            setTenDvi(mapDmucDvi.getOrDefault(getMaDvi(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucVthh;

    public void setMapDmucVthh(Map<String, String> mapDmucVthh) {
        this.mapDmucVthh = mapDmucVthh;
        if (!DataUtils.isNullObject(getLoaiVthh())) {
            setTenLoaiVthh(mapDmucVthh.getOrDefault(getLoaiVthh(), null));
        }
        if (!DataUtils.isNullObject(getCloaiVthh())) {
            setTenCloaiVthh(mapDmucVthh.getOrDefault(getCloaiVthh(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucLoaiXuat;

    public void setMapDmucLoaiXuat(Map<String, String> mapDmucLoaiXuat) {
        this.mapDmucLoaiXuat = mapDmucLoaiXuat;
        if (!DataUtils.isNullObject(getLoaiHinhNx())) {
            setTenLoaiHinhNx(mapDmucLoaiXuat.getOrDefault(getLoaiHinhNx(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucKieuXuat;

    public void setMapDmucKieuXuat(Map<String, String> mapDmucKieuXuat) {
        this.mapDmucKieuXuat = mapDmucKieuXuat;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapDmucKieuXuat.getOrDefault(getKieuNx(), null));
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapDmucThanhToan;

    public void setMapDmucThanhToan(Map<String, String> mapDmucThanhToan) {
        this.mapDmucThanhToan = mapDmucThanhToan;
        if (!DataUtils.isNullObject(getPthucTtoan())) {
            setTenPthucTtoan(mapDmucThanhToan.getOrDefault(getPthucTtoan(), null));
        }
    }

    public String getTrangThaiTh() {
        setTenTrangThaiTh(TrangThaiAllEnum.getLabelById(trangThaiTh));
        return trangThaiTh;
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhDxKhBanDauGia.TABLE_NAME + "_DINH_KEM'")
    private Set<FileDinhKemJoinTable> fileDinhKem = new HashSet<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            Set<FileDinhKemJoinTable> uniqueFiles = new HashSet<>(fileDinhKem);
            for (FileDinhKemJoinTable file : uniqueFiles) {
                file.setDataType(XhDxKhBanDauGia.TABLE_NAME + "_DINH_KEM");
                file.setXhDxKhBanDauGia(this);
            }
            this.fileDinhKem.addAll(uniqueFiles);
        }
    }

    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();
}