package com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
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
import java.util.stream.Collectors;

@Entity
@Table(name = XhDgBangKeHdr.TABLE_NAME)
@Data
public class XhDgBangKeHdr implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DG_BANG_KE_HDR";
    @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDgBangKeHdr.TABLE_NAME + "_SEQ")
//  @SequenceGenerator(sequenceName = XhDgBangKeHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhDgBangKeHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String maQhNs;
    private String soBangKeHang;
    private LocalDate ngayLapBangKe;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiHinhKho;
    private String nguoiGiamSat;
    private Long idThuKho;
    private Long idLanhDaoChiCuc;
    private Long idPhieuXuatKho;
    private String soPhieuXuatKho;
    private Long idPhieuKiemNghiem;
    private String soPhieuKiemNghiem;
    private LocalDate ngayKiemNghiemMau;
    private String tenNguoiGiao;
    private String cmtNguoiGiao;
    private String congTyNguoiGiao;
    private String diaChiNguoiGiao;
    private LocalDate thoiGianGiaoNhan;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private String donViTinh;
    private BigDecimal tongTrongLuongBi;
    private BigDecimal tongTrongLuongCaBi;
    private BigDecimal tongTrongTruBi;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private String trangThai;
    private String LyDoTuChoi;
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
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoChiCuc;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNhapXuat;
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
        if (!DataUtils.isNullObject(getMaDiemKho())) {
            setTenDiemKho(mapDmucDvi.containsKey(getMaDiemKho()) ? mapDmucDvi.get(getMaDiemKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNhaKho())) {
            setTenNhaKho(mapDmucDvi.containsKey(getMaNhaKho()) ? mapDmucDvi.get(getMaNhaKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaNganKho())) {
            setTenNganKho(mapDmucDvi.containsKey(getMaNganKho()) ? mapDmucDvi.get(getMaNganKho()) : null);
        }
        if (!DataUtils.isNullObject(getMaLoKho())) {
            setTenLoKho(mapDmucDvi.containsKey(getMaLoKho()) ? mapDmucDvi.get(getMaLoKho()) : null);
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
        if (!DataUtils.isNullObject(getKieuNhapXuat())) {
            setTenKieuNhapXuat(mapKieuNhapXuat.containsKey(getKieuNhapXuat()) ? mapKieuNhapXuat.get(getKieuNhapXuat()) : null);
        }
    }

    public String getTrangThai() {
        setTenTrangThai(TrangThaiAllEnum.getLabelById(trangThai));
        return trangThai;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhDgBangKeHdr.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhDgBangKeHdr.TABLE_NAME + "_DINH_KEM");
                s.setXhDgBangKeHdr(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @Transient
    private List<XhDgBangKeDtl> children = new ArrayList<>();
}
