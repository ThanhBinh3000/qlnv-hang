package com.tcdt.qlnvhang.entities.xuathang.daugia.ktracluong.bienbanlaymau;

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
@Table(name = XhBbLayMau.TABLE_NAME)
@Data
public class XhBbLayMau implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_BB_LAY_MAU";
    @Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhBbLayMau.TABLE_NAME + "_SEQ")
//	@SequenceGenerator(sequenceName = XhBbLayMau.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhBbLayMau.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String loaiBienBan;
    private String maQhNs;
    private String soBbLayMau;
    private LocalDate ngayLayMau;
    private Long idQdNv;
    private String soQdNv;
    private LocalDate ngayKyQdNv;
    private Long idQdNvDtl;
    private LocalDate tgianGiaoHang;
    private Long idHopDong;
    private String soHopDong;
    private LocalDate ngayKyHopDong;
    private String toChucCaNhan;
    private Long idKho;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private String loaiHinhNx;
    private String kieuNhapXuat;
    private String loaiVthh;
    private String cloaiVthh;
    private String tenHangHoa;
    private Long idThuKho;
    private Long idKtvBaoQuan;
    private String truongBpKtbq;
    private Long idLanhDaoChiCuc;
    private String donViKnghiem;
    private String diaDiemLayMau;
    private BigDecimal soLuongKiemTra;
    private String donViTinh;
    private Boolean ketQuaNiemPhong;
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
    private Long idHaoDoi;
    private String soBbHaoDoi;
    private Long idTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayXuatDocKho;
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
    private String tenNganLoKho;
    @Transient
    private String tenKtvBaoQuan;
    @Transient
    private String tenLoaiHinhNx;
    @Transient
    private String tenKieuNhapXuat;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenThuKho;
    @Transient
    private String tenLanhDaoChiCuc;
    @Transient
    private String tenTrangThai;
    @Transient
    private String phuongPhapLayMau;
    @Transient
    private String chiTieuKiemTra;
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
        if (!DataUtils.isNullObject(getKieuNhapXuat())) {
            setTenKieuNhapXuat(mapKieuNhapXuat.containsKey(getKieuNhapXuat()) ? mapKieuNhapXuat.get(getKieuNhapXuat()) : null);
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
    @Where(clause = "data_type='" + XhBbLayMau.TABLE_NAME + "_CAN_CU'")
    private List<FileDinhKemJoinTable> fileCanCu = new ArrayList<>();

    public void setFileCanCu(List<FileDinhKemJoinTable> fileCanCu) {
        this.fileCanCu.clear();
        if (!DataUtils.isNullObject(fileCanCu)) {
            fileCanCu.forEach(s -> {
                s.setDataType(XhBbLayMau.TABLE_NAME + "_CAN_CU");
                s.setXhBbLayMau(this);
            });
            this.fileCanCu.addAll(fileCanCu);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhBbLayMau.TABLE_NAME + "_DINH_KEM'")
    private List<FileDinhKemJoinTable> fileDinhKem = new ArrayList<>();

    public void setFileDinhKem(List<FileDinhKemJoinTable> fileDinhKem) {
        this.fileDinhKem.clear();
        if (!DataUtils.isNullObject(fileDinhKem)) {
            fileDinhKem.forEach(s -> {
                s.setDataType(XhBbLayMau.TABLE_NAME + "_DINH_KEM");
                s.setXhBbLayMau(this);
            });
            this.fileDinhKem.addAll(fileDinhKem);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "dataId")
    @Where(clause = "data_type='" + XhBbLayMau.TABLE_NAME + "_NIEM_PHONG'")
    private List<FileDinhKemJoinTable> fileNiemPhong = new ArrayList<>();

    public void setFileNiemPhong(List<FileDinhKemJoinTable> fileNiemPhong) {
        this.fileNiemPhong.clear();
        if (!DataUtils.isNullObject(fileNiemPhong)) {
            fileNiemPhong.forEach(s -> {
                s.setDataType(XhBbLayMau.TABLE_NAME + "_NIEM_PHONG");
                s.setXhBbLayMau(this);
            });
            this.fileNiemPhong.addAll(fileNiemPhong);
        }
    }

    @Transient
    private List<XhBbLayMauCt> children = new ArrayList<>();
}