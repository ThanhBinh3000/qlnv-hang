package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDKemJoinHoSoKyThuatDtl;
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
@Table(name = XhDxKhBanDauGia.TABLE_NAME)
@Data
public class XhDxKhBanDauGia extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_SEQ")
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
            setTenDvi(mapDmucDvi.containsKey(getMaDvi()) ? mapDmucDvi.get(getMaDvi()) : null);
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
    private Map<String, String> mapKieuNx;

    public void setMapKieuNx(Map<String, String> mapKieuNx) {
        this.mapKieuNx = mapKieuNx;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenKieuNx(mapKieuNx.containsKey(getKieuNx()) ? mapKieuNx.get(getKieuNx()) : null);
        }
    }

    @JsonIgnore
    @Transient
    private Map<String, String> mapPhuongThucTt;

    public void setMapPhuongThucTt(Map<String, String> mapPhuongThucTt) {
        this.mapPhuongThucTt = mapPhuongThucTt;
        if (!DataUtils.isNullObject(getKieuNx())) {
            setTenPthucTtoan(mapPhuongThucTt.containsKey(getPthucTtoan()) ? mapPhuongThucTt.get(getPthucTtoan()) : null);
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
    private List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDKemJoinHoSoKyThuatDtl> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhDxKhBanDauGia.TABLE_NAME + "_DINH_KEM");
                s.setXhDxKhBanDauGia(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @Transient
    private List<XhDxKhBanDauGiaDtl> children = new ArrayList<>();
}
